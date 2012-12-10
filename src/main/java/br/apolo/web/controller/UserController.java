package br.apolo.web.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.service.UserService;
import br.apolo.data.model.User;
import br.apolo.security.SecuredEnum;
import br.apolo.security.UserPermission;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	@Autowired
	UserService userService;
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView(Navigation.USER_INDEX.getPath());
		
		mav.addObject("user", getAuthenticatedUser());
		mav.addObject("isEditable", false);
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "change-password", method = RequestMethod.GET)
	public ModelAndView changePassword() {
		ModelAndView mav = new ModelAndView(Navigation.USER_CHANGE_PASSWORD.getPath());
		
		mav.addObject("user", getAuthenticatedUser());
		mav.addObject("isEditable", false);
		mav.addObject("changePassword", true);
		return mav;
	}

	@SecuredEnum(UserPermission.USER_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView(Navigation.USER_NEW.getPath());
		
		mav.addObject("user", new User());
		mav.addObject("isEditable", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_EDIT)
	@RequestMapping(value = "edit/{1}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_NEW.getPath());
		
		User user = userService.find(id);
		
		mav.addObject("user", user);
		mav.addObject("isEditable", true);
		
		return mav;
	}
	
	@SecuredEnum({ UserPermission.USER_CREATE, UserPermission.USER_EDIT })
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@ModelAttribute("user") User user) throws IOException {
		if (user != null) {
			userService.save(user);
		}
		
		return redirect(Navigation.HOME);
	}
	
	@SecuredEnum(UserPermission.USER_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		List<User> userList = userService.list();
		
		mav.addObject("userList", userList);
		
		return mav;
	}
	
}
