package br.apolo.web.controller;

import javax.servlet.jsp.JspTagException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import br.apolo.common.exception.GenericException;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.model.BaseEntity;
import br.apolo.web.enums.Navigation;

public abstract class BaseController<E extends BaseEntity> { 
	
	protected static final Logger log = LoggerFactory.getLogger(BaseController.class);
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path", method = RequestMethod.GET)
	 * @return ModelAndView
	 */
	public abstract ModelAndView list();
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum({ UserPermission.permission_name }) 
	 * @RequestMapping(value = "path", method = RequestMethod.POST)
	 * @param entity
	 * @return ModelAndView
	 */
	public abstract ModelAndView save(@ModelAttribute("entity") E entity);
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path", method = RequestMethod.GET)
	 * @return ModelAndView
	 */
	public abstract ModelAndView create();
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path/{id}", method = RequestMethod.GET)
	 * @param id
	 * @return ModelAndView
	 */
	public abstract ModelAndView edit(@PathVariable Long id);
	
	/**
	 * In implementation use annotation like this example:
	 * @SecuredEnum(UserPermission.permission_name) 
	 * @RequestMapping(value = "path/{id}", method = RequestMethod.GET)
	 * @param id
	 * @return ModelAndView
	 */
	public abstract ModelAndView remove(@PathVariable Long id);

	
	public String redirect(Navigation nav) {
		String path = "redirect:";
		
		if (nav != null) {
			path += nav.getPath();
		} else {
			path += Navigation.HOME.getPath();
		}
		
		return path;
	}

	@ExceptionHandler(GenericException.class)
	public ModelAndView handleGenericException(Exception ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 999);
		mav.addObject("title", MessageBundle.getMessageBundle("error.999"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.999.msg"));
		mav.addObject("exception", ex);

		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 999);
		mav.addObject("title", MessageBundle.getMessageBundle("error.999"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.999.msg"));
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
