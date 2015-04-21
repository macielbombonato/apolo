package apolo.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import apolo.business.service.UserGroupService;
import apolo.web.enums.Navigation;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.model.UserGroup;
import apolo.security.SecuredEnum;
import apolo.security.UserPermission;

@Controller
@RequestMapping(value = "/{tenant-url}/user-group")
public class UserGroupController extends BaseController<UserGroup> {

	@Autowired
	private UserGroupService userGroupService;
	
	@SecuredEnum({
			UserPermission.USER_PERMISSION_CREATE, 
			UserPermission.USER_PERMISSION_EDIT 
		})
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(
				@PathVariable("tenant-url") String tenant, 
				@Valid @ModelAttribute("entity") UserGroup entity, 
				BindingResult result, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_CREATE, 
				UserPermission.USER_PERMISSION_EDIT 
			);
		
		ModelAndView mav = new ModelAndView();
		
		/*
		 * Object validation
		 */
		if (result.hasErrors()) {
			mav.setViewName(
					getRedirectionPath(
							tenant, 
							request, 
							Navigation.USER_PERMISSION_NEW, 
							Navigation.USER_PERMISSION_EDIT
						)
				);
			
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
				
				mav = view(tenant, entity.getId(), request);
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
			} catch (AccessDeniedException e) {
				mav = list(tenant, request);
				mav.addObject("error", true);
				mav.addObject("message", e.getCustomMsg());
			}
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_LIST 
			);
		
		return list(tenant, 1, request);
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_LIST 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		Page<UserGroup> page = userGroupService.list(getDBTenant(tenant), pageNumber);
		
	    configurePageable(tenant, mav, page, "/user-group/list");
	    
	    mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userGroupList", page.getContent());	
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_PERMISSION_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_CREATE 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_NEW.getPath());
		
		UserGroup userGroup = new UserGroup();
		
		userGroup.setCreatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setCreationDate(new Date());
		
		userGroup.setLastUpdatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setLastUpdateDate(new Date());
		
		userGroup.setTenant(getDBTenant(tenant));
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", userGroupService.getUserPermissionList());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_EDIT 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_EDIT.getPath());
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
		userGroup.setLastUpdatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setLastUpdateDate(new Date());
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", userGroupService.getUserPermissionList());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_REMOVE 
			);
		
		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
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
	
	@SecuredEnum(UserPermission.USER_PERMISSION_REMOVE)
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_REMOVE 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
		if (userGroup != null) {
			try {
				userGroupService.remove(userGroup);

				mav = list(tenant, request);
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.remove.success"));
			} catch (Throwable e) {
				mav = list(tenant, request);
				mav.addObject("error", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.remove.msg.error"));
			}
		}
		
		return mav;
	}
	
	@SecuredEnum({
			UserPermission.USER_PERMISSION_VIEW, 
			UserPermission.USER_PERMISSION_LIST, 
			UserPermission.USER_LIST
		})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_VIEW, 
				UserPermission.USER_PERMISSION_LIST, 
				UserPermission.USER_LIST
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_VIEW.getPath());
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("readOnly", true);
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@ModelAttribute("searchParameter") String searchParameter, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_LIST
			);
		
		return search(tenant, 1, searchParameter, request);
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_LIST
			);
		
		return search(tenant, pageNumber, "", request);
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				@PathVariable String searchParameter, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_LIST
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		Page<UserGroup> page = userGroupService.search(getDBTenant(tenant), pageNumber, searchParameter);
		
		String url = "";
		
		if (searchParameter == null || "".equalsIgnoreCase(searchParameter)) {
			url = "/user-group/search";
		} else {
			url = "/user-group/search/"+searchParameter;
		}
		
		configurePageable(tenant, mav, page, url);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userGroupList", page.getContent());	
		}
		
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_LIST 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_SEARCH.getPath());
		
		return mav;
	}
	
	@SecuredEnum({
			UserPermission.USER_PERMISSION_EDIT 
		})
	@RequestMapping(value = "lock/{id}", method = RequestMethod.GET)
	public ModelAndView lock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_EDIT
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_VIEW.getPath());
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
		if (userGroup != null) {
			
			userGroupService.lock(userGroup);
			
			mav = view(tenant, userGroup.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@SecuredEnum({
			UserPermission.USER_PERMISSION_EDIT 
		})
	@RequestMapping(value = "unlock/{id}", method = RequestMethod.GET)
	public ModelAndView unlock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_PERMISSION_EDIT
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_VIEW.getPath());
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
		if (userGroup != null) {
			
			userGroupService.unlock(userGroup);
			
			mav = view(tenant, userGroup.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
}
