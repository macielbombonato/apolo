package br.apolo.web.controller;

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

import br.apolo.business.service.UserGroupService;
import br.apolo.common.exception.AccessDeniedException;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.enums.UserPermission;
import br.apolo.data.model.UserGroup;
import br.apolo.security.SecuredEnum;
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
		if (result.hasErrors() || entityHasErrors(entity)) {
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
			
			message.append(additionalValidation(entity));
			
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
		return list(1, request);
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable Integer pageNumber, HttpServletRequest request) {
		breadCrumbService.addNode(Navigation.USER_PERMISSION_LIST, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		Page<UserGroup> page = userGroupService.list(pageNumber);
		
	    configurePageable(mav, page, "/user-group/list");
	    
	    mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userGroupList", page.getContent());	
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_PERMISSION_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(HttpServletRequest request) {
		breadCrumbService.addNode(Navigation.USER_PERMISSION_NEW, request);
		
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
		breadCrumbService.addNode(Navigation.USER_PERMISSION_EDIT, request);
		
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
	
	@SecuredEnum(UserPermission.USER_PERMISSION_REMOVE)
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(@PathVariable Long id, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		if (userGroup != null) {
			try {
				userGroupService.remove(userGroup);

				mav = list(request);
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.remove.success"));
			} catch (Throwable e) {
				mav = list(request);
				mav.addObject("error", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.remove.msg.error"));
			}
		}
		
		return mav;
	}
	
	@SecuredEnum({UserPermission.USER_PERMISSION_VIEW, UserPermission.USER_PERMISSION_LIST, UserPermission.USER_LIST})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long id, HttpServletRequest request) {
		breadCrumbService.addNode(Navigation.USER_PERMISSION_VIEW, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_VIEW.getPath());
		
		UserGroup userGroup = userGroupService.find(id);
		
		mav.addObject("userGroup", userGroup);
		mav.addObject("readOnly", true);
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(@ModelAttribute("searchParameter") String searchParameter, HttpServletRequest request) {
		return search(1, searchParameter, request);
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(@PathVariable Integer pageNumber, HttpServletRequest request) {
		return search(pageNumber, "", request);
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(@PathVariable Integer pageNumber, @PathVariable String searchParameter, HttpServletRequest request) {
		breadCrumbService.addNode(Navigation.USER_PERMISSION_LIST, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_LIST.getPath());
		
		Page<UserGroup> page = userGroupService.search(pageNumber, searchParameter);
		
		String url = "";
		
		if (searchParameter == null || "".equalsIgnoreCase(searchParameter)) {
			url = "/user-group/search";
		} else {
			url = "/user-group/search/"+searchParameter;
		}
		
		configurePageable(mav, page, url);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userGroupList", page.getContent());	
		}
		
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_PERMISSION_LIST)
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(HttpServletRequest request) {
		breadCrumbService.addNode(Navigation.USER_PERMISSION_SEARCH, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_PERMISSION_SEARCH.getPath());
		
		return mav;
	}

    private boolean entityHasErrors(UserGroup entity) {
		boolean hasErrors = false;
		
		if (entity != null) {
			if (validateName(entity)) {
				hasErrors = true;
			}
		}
		
		return hasErrors;
	}
	
    private boolean validateName(UserGroup entity){
    	boolean hasError = false;
    	
    	UserGroup result = userGroupService.findByName(entity.getName());
		
		if(result != null 
				&& !result.getId().equals(entity.getId())){
			hasError = true;
		}
		
		return hasError;
    }
    
    private String additionalValidation(UserGroup entity) {
		StringBuilder message = new StringBuilder();
		
		if (entity != null) {
			if (validateName(entity)) {
				message.append(MessageBundle.getMessageBundle("user.group.name") + ": " + MessageBundle.getMessageBundle("user.group.name.duplicate") + "\n <br />");
			}
		}
		
		return message.toString();
	}
}
