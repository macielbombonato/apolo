package br.apolo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.common.exception.GenericException;
import br.apolo.web.enums.Navigation;

public abstract class BaseController { 
	
	protected static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

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

		ModelAndView mav = new ModelAndView("GenericExceptionPage");
		mav.addObject("exception", ex);

		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		ex.printStackTrace();

		ModelAndView mav = new ModelAndView("error");
		mav.addObject("exception", ex);

		return mav;
	}
}
