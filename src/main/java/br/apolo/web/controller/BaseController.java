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
		return redirect(nav, false);
	}
	
	public String redirect(Navigation nav, boolean redirect) {
		String path = "";
		
		if (redirect) {
			path += "redirect:";
		}
		
		if (nav != null) {
			path += nav.getPath();
		} else {
			path += Navigation.HOME.getPath();
		}
		
		return path;
	}

	protected String extractFileExtension(String fileName) {
		String extension = ""; //coelho: nao precisa inicializar aqui.

		/*
		 * Split and replaceAll have problems when we use dot ('.'). To resolve this problems, first we change dot to another
		 * character and split using this character.
		 */
		fileName = fileName.replace(".", "@@");
		String[] splitedFileName = fileName.split("@@");
		extension = splitedFileName[splitedFileName.length - 1];

		return "." + extension.toLowerCase();
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
