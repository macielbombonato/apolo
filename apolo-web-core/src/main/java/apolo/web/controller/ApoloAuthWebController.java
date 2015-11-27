package apolo.web.controller;

import apolo.business.service.UserService;
import apolo.common.util.MessageBundle;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.web.controller.base.BaseWebController;
import apolo.web.enums.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "/web")
public class ApoloAuthWebController extends BaseWebController {

	private Facebook facebook;

	@Autowired
	private UserService userService;

	@Autowired
	private UserWebController userController;

	@Autowired
	private ApoloWebController apoloController;

	@Inject
	public ApoloAuthWebController(Facebook facebook) {
		this.facebook = facebook;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		return login(null, request);
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant-url}/login", method = RequestMethod.GET)
	public ModelAndView login(@PathVariable("tenant-url") String tenant, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.AUTH_LOGIN.getPath());

		if (userService.getAuthenticatedUser() != null
				&& userService.getAuthenticatedUser().getPermissions() != null
				&& !userService.getAuthenticatedUser().getPermissions().isEmpty()) {
			mav = userController.index(tenant, request);
		}

		// The session ID is stored in spring context to be used in services call
		// don't remove this line. if do, the second factor will not work right 
		ContextLoader.getCurrentWebApplicationContext().getServletContext().setAttribute(
				"sessionId",
				request.getSession().getId()
		);

		ContextLoader.getCurrentWebApplicationContext().getServletContext().setAttribute(
				"tenant",
				getDBTenant(tenant)
		);

		mav.addObject("tenant", getDBTenant(tenant));

		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public ModelAndView loginFailed(HttpServletRequest request, ModelMap model) {
		return loginFailed(applicationProperties.getDefaultTenant(), request, model);
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant-url}/loginfailed", method = RequestMethod.GET)
	public ModelAndView loginFailed(
			@PathVariable("tenant-url") String tenant,
			HttpServletRequest request,
			ModelMap model) {
		ModelAndView mav = new ModelAndView(Navigation.AUTH_LOGIN.getPath(), model);

		String message = MessageBundle.getMessageBundle("error.403.1");

		// If the tenant is locked, the system will throw an exception
		Object object = request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

		if (object != null) {
			if (object instanceof BadCredentialsException) {
				message = ((BadCredentialsException) object).getMessage();
			}
		}

		mav.addObject("error", true);
		mav.addObject("message", message);
		mav.addObject("tenant", getDBTenant(tenant));

		return mav;
	}

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, ModelMap model) {
		return logout(applicationProperties.getDefaultTenant(), request, model);
	}

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "{tenant-url}/logout", method = RequestMethod.GET)
	public ModelAndView logoutWithoutWeb(
			@PathVariable("tenant-url") String tenant,
			HttpServletRequest request,
			ModelMap model) {
		return logout(tenant, request, model);
	}

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "web/{tenant-url}/logout", method = RequestMethod.GET)
	public ModelAndView logout(
			@PathVariable("tenant-url") String tenantUrl,
			HttpServletRequest request,
			ModelMap model) {

		ModelAndView mav = apoloController.indexWebTenant(tenantUrl, request);

		Tenant tenant = getDBTenant(tenantUrl);

		logout();

		mav.setViewName("redirect:" + "/web/" + tenant.getUrl());

		mav.addObject("tenant", tenant);
		request.getSession().setAttribute("tenant", tenant);

		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	public ModelAndView forgotPassword(
			HttpServletRequest request
	) {
		ModelAndView mav = new ModelAndView(Navigation.USER_FORGOT_PASSWORD.getPath());

		mav.addObject("tenant", getDBTenant(applicationProperties.getDefaultTenant()));

		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant-url}/forgot-password", method = RequestMethod.GET)
	public ModelAndView forgotPassword(
			@PathVariable("tenant-url") String tenant,
			HttpServletRequest request
	) {
		ModelAndView mav = new ModelAndView(Navigation.USER_FORGOT_PASSWORD.getPath());

		mav.addObject("tenant", getDBTenant(tenant));

		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant-url}/forgot-password-send", method = RequestMethod.POST)
	public ModelAndView forgotPasswordSend(
			@PathVariable("tenant-url") String tenant,
			@RequestParam(defaultValue = "") String email,
			HttpServletRequest request
	) {

		userService.generateResetPasswordToken(
				this.getServerUrl(
						request,
						tenant
				),
				email
		);

		ModelAndView mav = apoloController.indexWebTenant(tenant, request);

		mav.addObject("warn", true);
		mav.addObject("message", MessageBundle.getMessageBundle("user.forgot-password.msg"));

		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant-url}/reset-password/{token}", method = RequestMethod.GET)
	public ModelAndView resetPassword(
			@PathVariable("tenant-url") String tenant,
			@PathVariable("token") String token,
			HttpServletRequest request
	) {
		ModelAndView mav = new ModelAndView(Navigation.USER_RESET_PASSWORD.getPath());

		mav.addObject("tenant", getDBTenant(tenant));
		mav.addObject("token", token);

		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant-url}/reset-password-send", method = RequestMethod.POST)
	public ModelAndView resetPasswordSend(
			@PathVariable("tenant-url") String tenant,
			@RequestParam(defaultValue = "") String token,
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String passwordConfirmation,
			HttpServletRequest request
	) {

		if (password.equals(passwordConfirmation)) {
			User user = userService.findByToken(token);

			user.setPassword(password);

			userService.save(
					getServerUrl(
							request,
							user.getDbTenant().getUrl()
					),
					user,
					true,
					null
			);
		}

		ModelAndView mav = apoloController.indexWebTenant(tenant, request);

		mav.addObject("msg", true);
		mav.addObject("message", MessageBundle.getMessageBundle("user.reset-password.msg"));

		return mav;
	}

	private void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
