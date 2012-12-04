package br.apolo.web.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.data.model.User;
import br.apolo.security.SecuredEnum;
import br.apolo.security.UserPermission;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return Navigation.USER_INDEX.getPath();
	}

	@SecuredEnum(UserPermission.USER_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView(Navigation.USER_NEW.getPath());
		
		return mav;
	}
	
	@SecuredEnum({ UserPermission.USER_CREATE, UserPermission.USER_EDIT })
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@ModelAttribute("user") User user) throws IOException {

		// TODO implementar
		
		return redirect(Navigation.HOME);
	}

}
