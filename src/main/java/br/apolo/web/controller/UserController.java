package br.apolo.web.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.service.UserGroupService;
import br.apolo.business.service.UserService;
import br.apolo.common.util.ApoloUtils;
import br.apolo.data.model.User;
import br.apolo.data.model.UserGroup;
import br.apolo.security.SecuredEnum;
import br.apolo.security.UserPermission;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserGroupService userGroupService;
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView(Navigation.USER_INDEX.getPath());
		
		mav.addObject("user", getAuthenticatedUser());
		mav.addObject("readOnly", true);
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "change-password", method = RequestMethod.GET)
	public ModelAndView changePassword() {
		ModelAndView mav = new ModelAndView(Navigation.USER_CHANGE_PASSWORD.getPath());
		
		mav.addObject("user", getAuthenticatedUser());
		mav.addObject("readOnly", true);
		mav.addObject("changePassword", true);
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "change-password-save", method = RequestMethod.POST)
	public String changePasswordSave(@ModelAttribute("user") User user) throws IOException {
		
		if (user != null) {
			User dbuser = userService.find(user.getId());
			
			dbuser.setPassword(user.getPassword());
			
			userService.save(dbuser, true);
		}
		
		return redirect(Navigation.HOME);
	}

	@SecuredEnum(UserPermission.USER_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView(Navigation.USER_NEW.getPath());
		
		mav.addObject("user", new User());
		mav.addObject("groupList", userGroupService.list());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_EDIT.getPath());
		
		User user = userService.find(id);
		
		mav.addObject("user", user);
		mav.addObject("groupList", userGroupService.list());
		mav.addObject("readOnly", false);
		mav.addObject("editing", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_LIST)
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_VIEW.getPath());
		
		User user = userService.find(id);
		
		mav.addObject("user", user);
		mav.addObject("readOnly", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public ModelAndView remove(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		User user = userService.find(id);
		
		if (!user.equals(getAuthenticatedUser())) {
			userService.remove(user);	
		} else {
			mav = list();
			
			mav.addObject("error", true);
			mav.addObject("message", ApoloUtils.getMessageBundle("user.msg.error.remove.yourself"));
		}
		
		return mav;
	}
	
	@SecuredEnum({ UserPermission.USER_CREATE, UserPermission.USER_EDIT })
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("user") User user, @RequestParam(defaultValue = "false") boolean changePassword) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		if (user != null) {
			userService.save(user, changePassword);
			
			mav = list();
			
			mav.addObject("msg", true);
			mav.addObject("message", ApoloUtils.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		List<User> userList = userService.list();
		
		mav.addObject("userList", userList);
		
		return mav;
	}
	
	@SecuredEnum({ UserPermission.USER_PERMISSION_CREATE, UserPermission.USER_PERMISSION_EDIT })
	@RequestMapping(value = "permission/save", method = RequestMethod.POST)
	public ModelAndView permissionSave(@ModelAttribute("userGroup") UserGroup userGroup) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		if (userGroup != null) {
			userGroupService.save(userGroup);
			
			mav = permissionList();
			
			mav.addObject("msg", true);
			mav.addObject("message", ApoloUtils.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "permission/list", method = RequestMethod.GET)
	public ModelAndView permissionList() {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		List<UserGroup> userGroupList = userGroupService.list();
		
		mav.addObject("userGroupList", userGroupList);
		mav.addObject("permissionList", UserPermission.values());
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_CREATE)
	@RequestMapping(value = "permission/new", method = RequestMethod.GET)
	public ModelAndView permissionCreate() {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_CREATE.getPath());
		
		mav.addObject("user", new UserGroup());
		mav.addObject("permissionList", UserPermission.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_EDIT)
	@RequestMapping(value = "permission/edit/{id}", method = RequestMethod.GET)
	public ModelAndView permissionEdit(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_EDIT.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", UserPermission.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_REMOVE)
	@RequestMapping(value = "permission/remove/{id}", method = RequestMethod.GET)
	public ModelAndView permissionRemove(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView();
		
		UserGroup userGroup = userGroupService.find(id);
		
		if (userGroup != null) {
			if (userGroup.getUsers() != null && !userGroup.getUsers().isEmpty()) {
				mav = permissionList();
				
				mav.addObject("error", true);
				mav.addObject("message", ApoloUtils.getMessageBundle("user.group.msg.error.has.associated.users"));
			} else {
				userGroupService.remove(userGroup);	
				
				mav = permissionList();
			}
		}
		
		return mav;
	}
	
}
