package br.apolo.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public ModelAndView save(@Valid @ModelAttribute("entity") UserGroup entity, BindingResult result, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		/*
		 * Object validation
		 */
		if (result.hasErrors()) {
			mav.setViewName(getRedirectionPath(request, Navigation.USER_PERMISSION_NEW, Navigation.USER_PERMISSION_EDIT));
			
			mav.addObject("userGroup", entity);
			mav.addObject("permissionList", userGroupService.getUserPermissionList());
			mav.addObject("readOnly", false);
			mav.addObject("error", true);
			
			StringBuilder message = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
				
				message.append(MessageBundle.getMessageBundle("common.field") + " " + MessageBundle.getMessageBundle("user.group." + argument.getDefaultMessage()) + ": " + error.getDefaultMessage() + "\n <br />");
			}
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (entity != null) {
			try {
				userGroupService.save(entity);
				
				mav = view(entity.getId(), request);
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
			} catch (AccessDeniedException e) {
				mav = list(request);
				mav.addObject("error", true);
				mav.addObject("message", e.getCustomMsg());
			}
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.usergroup.list"), 1, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		List<UserGroup> userGroupList = userGroupService.list();
		
		mav.addObject("userGroupList", userGroupList);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.usergroup.new"), 1, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_NEW.getPath());
		
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
	public ModelAndView edit(@PathVariable Long id, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.usergroup.edit"), 2, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_EDIT.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		userGroup.setLastUpdatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setLastUpdateDate(new Date());
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", userGroupService.getUserPermissionList());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@Override
	@SecuredEnum(UserPermission.USER_PERMISSION_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(@PathVariable Long id) {
		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		UserGroup userGroup = userGroupService.find(id);
		
		if (userGroup != null) {
			if (userGroup.getUsers() != null && !userGroup.getUsers().isEmpty()) {
				result = MessageBundle.getMessageBundle("user.group.msg.error.has.associated.users");
				jsonItem.put("success", false);
			} else {
				try {
					userGroupService.remove(userGroup);
					
					result = MessageBundle.getMessageBundle("common.msg.remove.success");
					jsonItem.put("success", true);
				} catch (Throwable e) {
					result = MessageBundle.getMessageBundle("common.remove.msg.error");
					jsonItem.put("success", false);
				}
			}
		}
		
		jsonItem.put("message", result);
		jsonSubject.accumulate("result", jsonItem);
		
		return jsonSubject.toString();
	}
	
	@SecuredEnum({UserPermission.USER_PERMISSION_VIEW, UserPermission.USER_PERMISSION_LIST, UserPermission.USER_LIST})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long id, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.usergroup"), 2, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_VIEW.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("readOnly", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(@ModelAttribute("param") String param, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.usergroup.list"), 2, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		SearchResult<UserGroup> result = userGroupService.search(param);
		
		List<UserGroup> userGroupList = result.getResults();
		
		mav.addObject("userGroupList", userGroupList);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.usergroup.search"), 1, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_SEARCH.getPath());
		
		return mav;
	}
	
}
