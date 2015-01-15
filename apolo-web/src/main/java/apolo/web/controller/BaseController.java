package apolo.web.controller;

import apolo.business.service.BaseService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.exception.BusinessException;
import apolo.common.exception.GenericException;
import apolo.common.util.MessageBundle;
import apolo.data.model.BaseEntity;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.security.CurrentUser;
import apolo.security.UserPermission;
import apolo.web.enums.Navigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@SuppressWarnings("rawtypes")
public abstract class BaseController<E extends BaseEntity> { 
	
	protected static final Logger log = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	protected TenantService tenantService;
	
	@Autowired
	protected UserService userService;
	
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
	
	protected void configurePageable(String tenant, ModelAndView mav, Page<E> page, String url) {
		int current = page.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, page.getTotalPages());
	    
	    mav.addObject("beginIndex", begin);
	    mav.addObject("endIndex", end);
	    mav.addObject("currentIndex", current);
	    
	    if (tenant != null 
	    		&& !tenant.isEmpty()) {
	    	mav.addObject("url", "/" + tenant + url);	
	    } else {
	    	mav.addObject("url", "/" + url);
	    }
	    
		mav.addObject("page", page);
	}
	
	protected Tenant getDBTenant(String url) {
		Tenant tenant = null;
		
		tenant = tenantService.findByUrl(url);
		
		return tenant;
	}
	
	protected boolean validatePermissions(UserPermission...permissions) {
		boolean result = false;
		
		User user = tenantService.getAuthenticatedUser();
		
		if (user != null 
				&& permissions != null 
				&& permissions.length > 0) {
			if (user.getPermissions().contains(UserPermission.ADMIN)) {
				result = true;
			} else {
				for (UserPermission userPermission : permissions) {
					if (user.getPermissions().contains(userPermission)) {
						result = true;
					}
				}				
			}
		}
		
		if (!result) {
			String message = MessageBundle.getMessageBundle("error.403.msg");
			throw new AccessDeniedException(message);
		}
		
		return result;
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
	public ModelAndView handleException(apolo.common.exception.AccessDeniedException ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		
		String errorCode = "403";
		
		if (ex.getPrincipalCode() != null) {
			errorCode = ex.getPrincipalCode()+"";
		}
		
		if (ex.getSecondCode() != null) {
			errorCode += "." + ex.getSecondCode();
		}
		
		mav.addObject("code", errorCode);
		mav.addObject("title", MessageBundle.getMessageBundle("error.403"));
		mav.addObject("message", ex.getCustomMsg());
		mav.addObject("exception", ex);
		
		return mav;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView handleException(AccessDeniedException ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 403);
		mav.addObject("title", MessageBundle.getMessageBundle("error.403"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.403.msg"));
		mav.addObject("exception", ex);
		
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
}
