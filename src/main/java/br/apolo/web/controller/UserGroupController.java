package br.apolo.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.service.UserGroupService;
import br.apolo.common.util.ApoloUtils;
import br.apolo.data.model.UserGroup;
import br.apolo.security.SecuredEnum;
import br.apolo.security.UserPermission;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/user-group")
public class UserGroupController extends BaseController<UserGroup> {

	@Autowired
	private UserGroupService userGroupService;
	
	@SecuredEnum({ UserPermission.USER_PERMISSION_CREATE, UserPermission.USER_PERMISSION_EDIT })
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("userGroup") UserGroup userGroup) {
		ModelAndView mav = new ModelAndView();
		
		if (userGroup != null) {
			userGroupService.save(userGroup);
			
			mav = list();
			
			mav.addObject("msg", true);
			mav.addObject("message", ApoloUtils.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		List<UserGroup> userGroupList = userGroupService.list();
		
		mav.addObject("userGroupList", userGroupList);
		mav.addObject("permissionList", UserPermission.values());
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_CREATE.getPath());
		
		mav.addObject("user", new UserGroup());
		mav.addObject("permissionList", UserPermission.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_EDIT.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", UserPermission.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public ModelAndView remove(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView();
		
		UserGroup userGroup = userGroupService.find(id);
		
		if (userGroup != null) {
			if (userGroup.getUsers() != null && !userGroup.getUsers().isEmpty()) {
				mav = list();
				
				mav.addObject("error", true);
				mav.addObject("message", ApoloUtils.getMessageBundle("user.group.msg.error.has.associated.users"));
			} else {
				userGroupService.remove(userGroup);	
				
				mav = list();
			}
		}
		
		return mav;
	}
	
}
