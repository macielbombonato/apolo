package apolo.web.controller;

import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.util.MessageBundle;
import apolo.data.model.Tenant;
import apolo.security.SecuredEnum;
import apolo.security.UserPermission;
import apolo.web.enums.Navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("rawtypes")
@Controller
public class AuthController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired 
	private AppController appController;

    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request) {
        return login(applicationProperties.getDefaultTenant(), request);
    }

	@RequestMapping(value = "/{tenant-url}/auth/login", method = RequestMethod.GET)
	public ModelAndView login(@PathVariable("tenant-url") String tenant, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.AUTH_LOGIN.getPath());
		
		if (userService.getAuthenticatedUser() != null
				&& userService.getAuthenticatedUser().getPermissions() != null
				&& !userService.getAuthenticatedUser().getPermissions().isEmpty()
				&& userService.getAuthenticatedUser().getPermissions().contains(UserPermission.AFTER_AUTH_USER)) {
			mav = userController.index("apolo", request);
		}

		// The session ID is stored in spring context to be used in services call
		// don't remove this line. if do, the second factor will not work right 
		ContextLoader.getCurrentWebApplicationContext().getServletContext().setAttribute(
				"sessionId", 
				request.getSession().getId()
			);
		
		return mav;
	}

    @RequestMapping(value = "/auth/loginfailed", method = RequestMethod.GET)
    public ModelAndView loginFailed(HttpServletRequest request, ModelMap model) {
        return loginFailed(applicationProperties.getDefaultTenant(), request, model);
    }

	@RequestMapping(value = "/{tenant-url}/auth/loginfailed", method = RequestMethod.GET)
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
		return mav;
	}

	@RequestMapping(value = "/auth/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, ModelMap model) {
		return logout(applicationProperties.getDefaultTenant(), request, model);
	}

    @RequestMapping(value = "/{tenant-url}/auth/logout", method = RequestMethod.GET)
    public ModelAndView logout(
            @PathVariable("tenant-url") String tenant,
            HttpServletRequest request,
            ModelMap model) {
        ModelAndView mav = new ModelAndView(Navigation.AUTH_LOGIN.getPath(), model);
        return mav;
    }
	
    private void logout() {
    	SecurityContextHolder.getContext().setAuthentication(null);
    }
}
