package apolo.web.controller.base;

import apolo.business.service.base.BaseService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.BusinessException;
import apolo.common.exception.GenericException;
import apolo.common.util.MessageBundle;
import apolo.data.model.base.BaseEntity;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.security.ApoloSecurityService;
import apolo.security.CurrentUser;
import apolo.security.UserPermission;
import apolo.web.controller.ApoloAuthWebController;
import apolo.web.enums.Navigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public abstract class BaseWebController<E extends BaseEntity> {

	protected static final Logger log = LoggerFactory.getLogger(BaseWebController.class);

	@Inject
	protected TenantService tenantService;

	@Inject
	protected UserService userService;

	@Inject
	protected ApplicationProperties applicationProperties;

	@Inject
	protected ApoloSecurityService apoloSecurityService;

	@InitBinder
	protected void dateBinder(WebDataBinder binder) {
		//The date format to parse or output your dates
		SimpleDateFormat dateFormat = new SimpleDateFormat(MessageBundle.getMessageBundle("common.datePattern"));
		//Create a new CustomDateEditor
		CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
		//Register it as custom editor for the Date type
		binder.registerCustomEditor(Date.class, editor);
	}

	protected boolean authenticatedUserHasPermission(BaseService<E> service, User editingUser, UserPermission neededPermission, boolean isEditing) {
		User authenticatedUser = service.getAuthenticatedUser();

		boolean result = authenticatedUser != null
				&& (authenticatedUser.getId().equals(editingUser.getId())
				|| (authenticatedUser.getPermissions().contains(UserPermission.ADMIN)
				|| authenticatedUser.getPermissions().contains(neededPermission)
		)
		);

		if (isEditing
				&& editingUser.getPermissions().contains(UserPermission.ADMIN)
				&& !authenticatedUser.getPermissions().contains(UserPermission.ADMIN)) {
			result = false;
		}

		return result;
	}

	protected String getServerUrl(HttpServletRequest request, String tenant) {
		String serverUrl = "";

		try {
			URL url = new URL(request.getRequestURL().toString());

			String host  = url.getHost();
			String userInfo = url.getUserInfo();
			String scheme = url.getProtocol();
			int port = url.getPort();
			String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
			String query = (String) request.getAttribute("javax.servlet.forward.query_string");
			URI uri = new URI(scheme,userInfo,host,port,path,query,null);

			serverUrl = uri.toString() + "/web/" + tenant + "/";

		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
		}

		return serverUrl;
	}

	protected void configurePageable(String tenant, ModelAndView mav, Page<E> page, String url) {
		int current = page.getNumber() + 1;
		int begin = Math.max(1, current - 5);
		int end = Math.min(begin + 10, page.getTotalPages());

		mav.addObject("beginIndex", begin);
		mav.addObject("endIndex", end);
		mav.addObject("currentIndex", current);

		if (tenant != null
				&& !tenant.isEmpty()) {
			mav.addObject("url", "/web/" + tenant + url);
		} else {
			mav.addObject("url", "/web/" + url);
		}

		mav.addObject("page", page);
	}

	protected Tenant getDBTenant(String url) {
		Tenant tenant = null;

		tenant = tenantService.getValidatedTenant(url);

		User user = userService.getAuthenticatedUser();

		if (user != null
				&& !user.getTenant().equals(tenant)) {

			if (user.getPermissions() != null
					&& !user.getPermissions().isEmpty()
					&& (
					user.getPermissions().contains(UserPermission.ADMIN)
							|| user.getPermissions().contains(UserPermission.TENANT_MANAGER)
			)) {
				user.getDbTenant();
				user.setTenant(tenant);

				reconstructAuthenticatedUser(user);
			} else {
				String message = MessageBundle.getMessageBundle("error.403.msg");
				throw new AccessDeniedException(message);
			}
		}

		return tenant;
	}

	protected void reconstructAuthenticatedUser(User user) {
		Collection<GrantedAuthority> authorities =
				userService.loadUserAuthorities(
						userService.getAuthenticatedUser()
				);

		Authentication newAuth = new CurrentUser(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				user,
				authorities
		);

		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	/**
	 * This method was created to be used in save methods.
	 * In error cases the system can redirect the user to origin path to fix data to submit it again.
	 * @param request
	 * @param firstOption - First Navigation enum option
	 * @param secondOption - Second Navigation enum option
	 * @return String - Origin path
	 */
	protected String getRedirectionPath(String tenant, HttpServletRequest request, Navigation firstOption, Navigation secondOption) {
		String referer = request.getHeader("referer");
		String redirectionPath = "";
		Navigation navChoice;
		if (referer != null && referer.contains(firstOption.getPath())) {
			redirectionPath = firstOption.getPath();
			navChoice = firstOption;
		} else {
			redirectionPath = secondOption.getPath();
			navChoice = secondOption;
		}

		return redirectionPath;
	}

	public String redirect(Navigation nav) {
		String path = "redirect:";

		if (nav != null) {
			path += nav.getPath();
		} else {
			path += Navigation.USER_INDEX.getPath();
		}

		return path;
	}

	@ExceptionHandler(BusinessException.class)
	public ModelAndView handleBusinessException(BusinessException ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		String errorCode = "500";

		if (ex.getPrincipalCode() != null) {
			errorCode = ex.getPrincipalCode()+"";
		}

		if (ex.getSecondCode() != null) {
			errorCode += "." + ex.getSecondCode();
		}

		mav.addObject("code", errorCode);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", ex.getCustomMsg());
		mav.addObject("exception", ex);

		return mav;
	}

	@ExceptionHandler(GenericException.class)
	public ModelAndView handleGenericException(GenericException ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		String errorCode = "500";

		if (ex.getPrincipalCode() != null) {
			errorCode = ex.getPrincipalCode()+"";
		}

		if (ex.getSecondCode() != null) {
			errorCode += "." + ex.getSecondCode();
		}

		mav.addObject("code", errorCode);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", ex.getCustomMsg());
		mav.addObject("exception", ex);

		return mav;
	}

	protected static final String builtPermissions(UserPermission... userPermissions) {
		String result = "hasAnyRole(";

		if (userPermissions != null && userPermissions.length > 0) {
			for(UserPermission permission : userPermissions) {
				if (!"hasAnyRole(".equals(result)) {
					result += ", ";
				}

				result += permission.getAttribute();
			}
		}

		result += ")";

		return result;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 500);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.500.msg"));
		mav.addObject("exception", ex);

		return mav;
	}

	@ExceptionHandler(apolo.common.exception.AccessDeniedException.class)
	public ModelAndView handleException(apolo.common.exception.AccessDeniedException ex, HttpServletRequest request) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());

		String errorCode = "403";

		if (ex.getPrincipalCode() != null) {
			errorCode = ex.getPrincipalCode()+"";
		}

		if (ex.getSecondCode() != null) {
			errorCode += "." + ex.getSecondCode();
		}

		if (apoloSecurityService.isAuthenticated()) {
			mav.addObject("code", errorCode);
			mav.addObject("title", MessageBundle.getMessageBundle("error.403"));
			mav.addObject("message", ex.getCustomMsg());
			mav.addObject("exception", ex);
		} else {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			ApoloAuthWebController apoloAuthController = (ApoloAuthWebController) ctx.getBean("authController");

			mav = apoloAuthController.login(request);
		}

		return mav;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView handleException(AccessDeniedException ex, HttpServletRequest request) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());

		if (apoloSecurityService.isAuthenticated()) {
			mav.addObject("code", 403);
			mav.addObject("title", MessageBundle.getMessageBundle("error.403"));
			mav.addObject("message", MessageBundle.getMessageBundle("error.403.msg"));
			mav.addObject("exception", ex);
		} else {
			ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();

			ApoloAuthWebController apoloAuthController = (ApoloAuthWebController) ctx.getBean("authController");

			mav = apoloAuthController.login(request);
		}

		return mav;
	}

	@ExceptionHandler(NestedServletException.class)
	public ModelAndView handleException(NestedServletException ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 500);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.500.msg"));
		mav.addObject("exception", ex);

		return mav;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		//Create a custom binder that will convert a String with pattern dd/MM/yyyy to an appropriate Date object.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

		binder.registerCustomEditor(Date.class, "createdAt", new CustomDateEditor(dateFormat, false));
		binder.registerCustomEditor(Date.class, "updatedAt", new CustomDateEditor(dateFormat, false));

		binder.registerCustomEditor(Date.class, "resetPasswordSentAt", new CustomDateEditor(dateFormat, false));

		binder.registerCustomEditor(Date.class, "currentSignInAt", new CustomDateEditor(dateFormat, false));
		binder.registerCustomEditor(Date.class, "lastSignInAt", new CustomDateEditor(dateFormat, false));

	}
}
