package apolo.web.controller;

import apolo.business.service.UserCustomFieldService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.FieldType;
import apolo.data.model.UserCustomField;
import apolo.security.SecuredEnum;
import apolo.security.UserPermission;
import apolo.web.enums.Navigation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/{tenant-url}/user-custom-field")
public class UserCustomFieldController extends BaseController<UserCustomField> {

	@Autowired
	private UserCustomFieldService userCustomFieldService;
	
	@SecuredEnum({ 
			UserPermission.USER_CUSTOM_FIELD_CREATE, 
			UserPermission.USER_CUSTOM_FIELD_EDIT 
		})
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(
				@PathVariable("tenant-url") String tenant, 
				@Valid @ModelAttribute("entity") UserCustomField entity, 
				BindingResult result, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_CREATE, 
				UserPermission.USER_CUSTOM_FIELD_EDIT 
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
							Navigation.USER_CUSTOM_FIELD_NEW, 
							Navigation.USER_CUSTOM_FIELD_EDIT
						)
				);
			
			mav.addObject("userCustomField", entity);
			mav.addObject("typeList", FieldType.values());
			mav.addObject("readOnly", false);
			mav.addObject("error", true);
			
			StringBuilder message = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
				
				message.append(MessageBundle.getMessageBundle("common.field") + " " + MessageBundle.getMessageBundle("user.custom.field." + argument.getDefaultMessage()) + ": " + error.getDefaultMessage() + "\n <br />");
			}
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (entity != null) {
			try {
				userCustomFieldService.save(entity);
				
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

	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_LIST 
			);
		
		return list(tenant, 1, request);
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_LIST 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_LIST.getPath());
		
		Page<UserCustomField> page = userCustomFieldService.list(getDBTenant(tenant), pageNumber);
		
	    configurePageable(tenant, mav, page, "/user-custom-field/list");
	    
	    mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userCustomFieldList", page.getContent());	
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(
				@PathVariable("tenant-url") String tenant,
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_CREATE
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_NEW.getPath());
		
		UserCustomField userCustomField = new UserCustomField();
		
		userCustomField.setCreatedBy(userCustomFieldService.getAuthenticatedUser());
		userCustomField.setCreatedAt(new Date());
		
		userCustomField.setUpdatedBy(userCustomFieldService.getAuthenticatedUser());
		userCustomField.setUpdatedAt(new Date());
		
		userCustomField.setTenant(getDBTenant(tenant));
		
		mav.addObject("userCustomField", userCustomField);
		mav.addObject("typeList", FieldType.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_EDIT 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_EDIT.getPath());
		
		UserCustomField userCustomField = userCustomFieldService.find(getDBTenant(tenant), id);
		
		userCustomField.setUpdatedBy(userCustomFieldService.getAuthenticatedUser());
		userCustomField.setUpdatedAt(new Date());
		
		mav.addObject("userCustomField", userCustomField);
		mav.addObject("typeList", FieldType.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(
				@PathVariable("tenant-url") String tenant,
				@PathVariable Long id
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_REMOVE 
			);
		
		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		UserCustomField userCustomField = userCustomFieldService.find(getDBTenant(tenant), id);
		
		if (userCustomField != null) {
			try {
				userCustomFieldService.remove(userCustomField);
				
				result = MessageBundle.getMessageBundle("common.msg.remove.success");
				jsonItem.put("success", true);
			} catch (Throwable e) {
				result = MessageBundle.getMessageBundle("common.remove.msg.error");
				jsonItem.put("success", false);
			}
		}
		
		jsonItem.put("message", result);
		jsonSubject.accumulate("result", jsonItem);
		
		return jsonSubject.toString();
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_REMOVE)
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_REMOVE 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_LIST.getPath());
		
		UserCustomField userCustomField = userCustomFieldService.find(getDBTenant(tenant), id);
		
		if (userCustomField != null) {
			try {
				userCustomFieldService.remove(userCustomField);

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
			UserPermission.USER_CUSTOM_FIELD_VIEW, 
			UserPermission.USER_CUSTOM_FIELD_LIST
		})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_VIEW, 
				UserPermission.USER_CUSTOM_FIELD_LIST 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_VIEW.getPath());
		
		UserCustomField userCustomField = userCustomFieldService.find(getDBTenant(tenant), id);
		
		mav.addObject("userCustomField", userCustomField);
		mav.addObject("typeList", FieldType.values());
		mav.addObject("readOnly", true);
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@ModelAttribute("searchParameter") String searchParameter, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_LIST 
			);
		
		return search(tenant, 1, searchParameter, request);
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_LIST 
			);
		
		return search(tenant, pageNumber, "", request);
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				@PathVariable String searchParameter, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_LIST 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_LIST.getPath());
		
		Page<UserCustomField> page = userCustomFieldService.search(getDBTenant(tenant), pageNumber, searchParameter);
		
		String url = "";
		
		if (searchParameter == null || "".equalsIgnoreCase(searchParameter)) {
			url = "/user-custom-field/search";
		} else {
			url = "/user-custom-field/search/"+searchParameter;
		}
		
		configurePageable(tenant, mav, page, url);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userCustomFieldList", page.getContent());	
		}
		
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		validatePermissions(
				UserPermission.USER_CUSTOM_FIELD_LIST 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_SEARCH.getPath());
		
		return mav;
	}
}
