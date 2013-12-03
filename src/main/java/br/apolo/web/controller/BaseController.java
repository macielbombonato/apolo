package br.apolo.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import br.apolo.business.service.BaseService;
import br.apolo.common.exception.BusinessException;
import br.apolo.common.exception.GenericException;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.enums.UserPermission;
import br.apolo.data.model.BaseEntity;
import br.apolo.data.model.User;
import br.apolo.web.enums.Navigation;
import br.apolo.web.service.BreadCrumbTreeService;

@SuppressWarnings("rawtypes")
public abstract class BaseController<E extends BaseEntity> { 
	
	protected static final Logger log = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	protected BreadCrumbTreeService breadCrumbService;
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path", method = RequestMethod.GET)
	 * @return ModelAndView
	 */
	public abstract ModelAndView list(HttpServletRequest request);
	
	public abstract ModelAndView list(@PathVariable Integer pageNumber, HttpServletRequest request);
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum({ UserPermission.permission_name }) 
	 * @RequestMapping(value = "path", method = RequestMethod.POST)
	 * @param entity
	 * @return ModelAndView
	 */
	public abstract ModelAndView save(E entity, BindingResult result, HttpServletRequest request);
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path", method = RequestMethod.GET)
	 * @return ModelAndView
	 */
	public abstract ModelAndView create(HttpServletRequest request);
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path/{id}", method = RequestMethod.GET)
	 * @param id
	 * @return ModelAndView
	 */
	public abstract ModelAndView edit(@PathVariable Long id, HttpServletRequest request);
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path/{id}", method = RequestMethod.GET)
	 * @param id
	 * @return String - Json with operation return message
	 */
	public abstract @ResponseBody String remove(@PathVariable Long id);
	
	@InitBinder
	protected void dateBinder(WebDataBinder binder) {
		//The date format to parse or output your dates
		SimpleDateFormat dateFormat = new SimpleDateFormat(MessageBundle.getMessageBundle("common.datePattern"));
	    //Create a new CustomDateEditor
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	    //Register it as custom editor for the Date type
	    binder.registerCustomEditor(Date.class, editor);
	}
	
	protected boolean authenticatedUserHasPermission(BaseService<E> service, Long id, UserPermission neededPermission) {
		User authenticatedUser = service.getAuthenticatedUser();
		
		return authenticatedUser != null 
				&& (authenticatedUser.getId().equals(id) 
						|| (authenticatedUser.getPermissions().contains(UserPermission.ADMIN) || authenticatedUser.getPermissions().contains(neededPermission)));
	}
	
	protected void configurePageable(ModelAndView mav, Page<E> page, String url) {
		int current = page.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, page.getTotalPages());
	    
	    mav.addObject("beginIndex", begin);
	    mav.addObject("endIndex", end);
	    mav.addObject("currentIndex", current);
	    mav.addObject("url", url);
		mav.addObject("page", page);
	}
	
	/**
	 * This method was created to be used in save methods.
	 * In error cases the system can redirect the user to origin path to fix data to submit it again.
	 * @param request
	 * @param firstOption - First Navigation enum option
	 * @param secondOption - Second Navigation enum option
	 * @return String - Origin path
	 */
	protected String getRedirectionPath(HttpServletRequest request, Navigation firstOption, Navigation secondOption) {
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
		
        if (referer == null || !referer.endsWith("save")) {
        	breadCrumbService.addNode(navChoice, request);        	
        }
		
		return redirectionPath;
	}
	
	public String redirect(Navigation nav) {
		String path = "redirect:";
		
		if (nav != null) {
			path += nav.getPath();
		} else {
			path += Navigation.HOME.getPath();
		}
		
		return path;
	}

	@ExceptionHandler(BusinessException.class)
	public ModelAndView handleBusinessException(BusinessException ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 500);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", ex.getCustomMsg());
		mav.addObject("exception", ex);

		return mav;
	}
	
	@ExceptionHandler(GenericException.class)
	public ModelAndView handleGenericException(Exception ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 500);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.500.msg"));
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
	
	@ExceptionHandler(JspTagException.class)
	public ModelAndView handleException(JspTagException ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 500);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.500.msg"));
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
