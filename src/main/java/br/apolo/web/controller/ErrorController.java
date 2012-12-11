package br.apolo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.common.util.ApoloUtils;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/error")
public class ErrorController {

	@RequestMapping(value = "403", method = RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 403);
		mav.addObject("message", ApoloUtils.getMessageBundle("error.403"));
		return mav;
	}
	
	@RequestMapping(value = "500", method = RequestMethod.GET)
	public ModelAndView internalError() {
		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 500);
		mav.addObject("message", ApoloUtils.getMessageBundle("error.500"));
		return mav;
	}

}
