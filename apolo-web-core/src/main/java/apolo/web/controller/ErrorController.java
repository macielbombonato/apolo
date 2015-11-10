package apolo.web.controller;

import apolo.common.util.MessageBundle;
import apolo.web.enums.Navigation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/web/error")
public class ErrorController {

	@RequestMapping(value = "403", method = RequestMethod.GET)
	public ModelAndView accessDenied(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 403);
		mav.addObject("title", MessageBundle.getMessageBundle("error.403"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.403.msg"));
		return mav;
	}
	
	@RequestMapping(value = "404", method = RequestMethod.GET)
	public ModelAndView notFound(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 404);
		mav.addObject("title", MessageBundle.getMessageBundle("error.404"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.404.msg"));
		return mav;
	}
	
	@RequestMapping(value = "500", method = RequestMethod.GET)
	public ModelAndView internalError(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.ERROR.getPath());
		mav.addObject("code", 500);
		mav.addObject("title", MessageBundle.getMessageBundle("error.500"));
		mav.addObject("message", MessageBundle.getMessageBundle("error.500.msg"));
		return mav;
	}
}
