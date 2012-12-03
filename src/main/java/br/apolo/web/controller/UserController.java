package br.apolo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.apolo.security.SecuredEnum;
import br.apolo.security.UserPermission;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController {

	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public String index() {
		return Navigation.USER_INDEX.getPath();
	}

}
