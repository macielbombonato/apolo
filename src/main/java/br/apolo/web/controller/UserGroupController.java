package br.apolo.web.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.UserGroupService;
import br.apolo.common.exception.AccessDeniedException;
import br.apolo.common.util.MessageBundle;
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
	public ModelAndView save(@ModelAttribute("userGroup") UserGroup userGroup, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		if (userGroup != null) {
			try {
				userGroupService.save(userGroup);
				
				mav = view(userGroup.getId());
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
			} catch (AccessDeniedException e) {
				mav = list();
				mav.addObject("error", true);
				mav.addObject("message", e.getCustomMsg());
			}
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		List<UserGroup> userGroupList = userGroupService.list();
		
		mav.addObject("userGroupList", userGroupList);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_CREATE.getPath());
		
		UserGroup userGroup = new UserGroup();
		
		userGroup.setCreatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setCreationDate(new Date());
		
		userGroup.setLastUpdatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setLastUpdateDate(new Date());
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", userGroupService.getUserPermissionList());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_EDIT.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		userGroup.setLastUpdatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setLastUpdateDate(new Date());
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", userGroupService.getUserPermissionList());
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
				mav.addObject("message", MessageBundle.getMessageBundle("user.group.msg.error.has.associated.users"));
			} else {
				try {
					userGroupService.remove(userGroup);
					
					mav = list();
					mav.addObject("msg", true);
					mav.addObject("message", MessageBundle.getMessageBundle("common.msg.remove.success"));
				} catch (AccessDeniedException e) {
					mav = list();
					mav.addObject("error", true);
					mav.addObject("message", e.getCustomMsg());
				}
			}
		}
		
		return mav;
	}
	
	@SecuredEnum({UserPermission.USER_PERMISSION_VIEW, UserPermission.USER_PERMISSION_LIST, UserPermission.USER_LIST})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_VIEW.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("readOnly", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(@ModelAttribute("param") String param) {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		SearchResult<UserGroup> result = userGroupService.search(param);
		
		List<UserGroup> userGroupList = result.getResults();
		
		mav.addObject("userGroupList", userGroupList);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm() {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_SEARCH.getPath());
		
		return mav;
	}
	
}
