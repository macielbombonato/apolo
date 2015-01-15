package apolo.web.controller;

import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.util.MessageBundle;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "/auth")
public class AuthController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired 
	private AppController appController;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
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
	
	@SecuredEnum({
			UserPermission.BEFORE_AUTH_USER, 
			UserPermission.AFTER_AUTH_USER
		})
	@RequestMapping(value = "second-factor", method = RequestMethod.GET)
	public ModelAndView secondFactor(
			HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView mav = new ModelAndView(Navigation.AUTH_SECOND_FACTOR.getPath());
		
		if (userService.getAuthenticatedUser() != null) {
			appController.authenticatedChangeLocale(
					userService.getAuthenticatedUser().getTenant().getUrl(), 
					userService.getAuthenticatedUser().getLanguage().getCode(), 
					request, 
					response
				);
			
			if (userService.getAuthenticatedUser().getPermissions() != null
					&& !userService.getAuthenticatedUser().getPermissions().isEmpty()
					&& userService.getAuthenticatedUser().getPermissions().contains(UserPermission.AFTER_AUTH_USER)) {
				
				mav = userController.index(userService.getAuthenticatedUser().getTenant().getUrl(), request);
			}
		}
		
		return mav;
	}


	@RequestMapping(value = "loginfailed", method = RequestMethod.GET)
	public ModelAndView loginFailed(HttpServletRequest request, ModelMap model) {
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

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, ModelMap model) {
		ModelAndView mav = new ModelAndView(Navigation.AUTH_LOGIN.getPath(), model);
		return mav;
	}
	
//	@SecuredEnum(UserPermission.BEFORE_AUTH_USER)
//	@RequestMapping(value = "validate-token", method = RequestMethod.POST)
//	public ModelAndView validateToken(
//				@ModelAttribute("token") String token,
//				BindingResult result, 
//				HttpServletRequest request
//			) {
//		ModelAndView mav = null;
//		
//		TokenStatus tokenValidation = asaWsCall.callValidateToken(
//				userService.getAuthenticatedUser().getEmail(), 
//				token
//			);
//		
//		if (TokenStatus.VALID.equals(tokenValidation)) {
//			login();
//			mav = userController.index("apolo", request);
//		} else {
//			logout();
//			mav = new ModelAndView(Navigation.AUTH_LOGIN.getPath());
//			mav.addObject("error", true);
//			mav.addObject("message", MessageBundle.getMessageBundle("user.auth.msg.error.invalid.token"));
//		}
//		
//		return mav;
//	}
	
//    private void login() {
//        Collection<GrantedAuthority> authorities = userService.loadUserAuthorities(userService.getAuthenticatedUser());
//        Authentication newAuth = new CurrentUser(
//        		userService.getAuthenticatedUser().getId(), 
//        		userService.getAuthenticatedUser().getEmail(), 
//        		userService.getAuthenticatedUser().getPassword(), 
//        		userService.getAuthenticatedUser(), 
//        		authorities
//        	);
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//    }
    
    private void logout() {
    	SecurityContextHolder.getContext().setAuthentication(null);
    }
}
