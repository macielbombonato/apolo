package br.apolo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.apolo.business.service.UserService;
import br.apolo.data.model.User;
import br.apolo.security.SecuredEnum;
import br.apolo.security.UserPermission;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "user")
@SecuredEnum(UserPermission.USER)
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public String index(UserDetails userDetails, Model model) {
		LOG.info(userDetails.toString());
		return redirect(Navigation.USER_INDEX);
	}
	
	@RequestMapping(value = "user.json", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public User jsonGetUser(UserDetails userDetails) {
		return userService.findByLogin(userDetails.getUsername());
	}

}
