package apolo.web.controller;

import apolo.business.service.UserGroupService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.model.UserGroup;
import apolo.web.controller.abstracts.BaseWebController;
import apolo.web.enums.Navigation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/web/{tenant-url}/user-group")
public class UserGroupWebController extends BaseWebController<UserGroup> {

	@Autowired
	private UserGroupService userGroupService;
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_CREATE', 'USER_PERMISSION_EDIT')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(
				@PathVariable("tenant-url") String tenant, 
				@Valid @ModelAttribute("entity") UserGroup entity, 
				BindingResult result, 
				HttpServletRequest request
			) {

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

	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_LIST')")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		return list(tenant, 1, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_LIST')")
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		Page<UserGroup> page = userGroupService.list(getDBTenant(tenant), pageNumber);
		
	    configurePageable(tenant, mav, page, "/user-group/list");
	    
	    mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userGroupList", page.getContent());	
		}
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_CREATE')")
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_NEW.getPath());
		
		UserGroup userGroup = new UserGroup();
		
		userGroup.setCreatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setCreatedAt(new Date());
		
		userGroup.setUpdatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setUpdatedAt(new Date());
		
		userGroup.setTenant(getDBTenant(tenant));
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", userGroupService.getUserPermissionList());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_EDIT')")
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_EDIT.getPath());
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
		userGroup.setUpdatedBy(userGroupService.getAuthenticatedUser());
		userGroup.setUpdatedAt(new Date());
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("permissionList", userGroupService.getUserPermissionList());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_REMOVE')")
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id
			) {

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
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_REMOVE')")
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

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
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_VIEW', 'USER_PERMISSION_LIST', 'USER_LIST')")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_VIEW.getPath());
		
		UserGroup userGroup = userGroupService.find(getDBTenant(tenant), id);
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("readOnly", true);
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_LIST')")
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@ModelAttribute("searchParameter") String searchParameter, 
				HttpServletRequest request
			) {

		return search(tenant, 1, searchParameter, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_LIST')")
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		return search(tenant, pageNumber, "", request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_LIST')")
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				@PathVariable String searchParameter, 
				HttpServletRequest request
			) {

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
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_LIST')")
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_SEARCH.getPath());
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_EDIT')")
	@RequestMapping(value = "lock/{id}", method = RequestMethod.GET)
	public ModelAndView lock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

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
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_PERMISSION_EDIT')")
	@RequestMapping(value = "unlock/{id}", method = RequestMethod.GET)
	public ModelAndView unlock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

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
