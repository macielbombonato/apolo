package apolo.web.controller;

import apolo.business.service.ConfigurationService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.model.Configuration;
import apolo.data.model.Tenant;
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
@RequestMapping(value = "/{tenant-url}/configuration")
public class ConfigurationController extends BaseController<Configuration> {

	@Autowired
	private ConfigurationService configurationService;
	
	@SecuredEnum({ 
			UserPermission.CONFIGURATION_CREATE, 
			UserPermission.CONFIGURATION_EDIT 
		})
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(
				@PathVariable("tenant-url") String tenantUrl, 
				@Valid @ModelAttribute("entity") Configuration entity, 
				BindingResult result, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_CREATE, 
				UserPermission.CONFIGURATION_EDIT 
			);
		
		ModelAndView mav = new ModelAndView();
		
		Tenant tenant = getDBTenant(tenantUrl);
		
		/*
		 * Object validation
		 */
		if (result.hasErrors() || entityHasErrors(tenant, entity)) {
			mav.setViewName(
					getRedirectionPath(
							tenantUrl, 
							request, 
							Navigation.CONFIGURATION_NEW, 
							Navigation.CONFIGURATION_EDIT
						)
				);
			
			mav.addObject("configuration", entity);
			mav.addObject("readOnly", false);
			mav.addObject("error", true);
			
			StringBuilder message = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
				
				message.append(
						MessageBundle.getMessageBundle("common.field") 
						+ " " + 
						MessageBundle.getMessageBundle("configuration." + argument.getDefaultMessage()) 
						+ ": " + 
						error.getDefaultMessage() + "\n <br />"
					);
			}
			
			message.append(additionalValidation(tenant, entity));
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (entity != null) {
			try {
				configurationService.save(entity);
				
				mav = view(tenantUrl, entity.getId(), request);
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
			} catch (AccessDeniedException e) {
				mav = list(tenantUrl, request);
				mav.addObject("error", true);
				mav.addObject("message", e.getCustomMsg());
			}
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.CONFIGURATION_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_LIST 
			);
		
		return list(tenant, 1, request);
	}
	
	@SecuredEnum(UserPermission.CONFIGURATION_LIST)
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_LIST 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.CONFIGURATION_LIST.getPath());
		
		Page<Configuration> page = configurationService.list(getDBTenant(tenant), pageNumber);
		
	    configurePageable(tenant, mav, page, "/configuration/list");
	    
	    mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("configurationList", page.getContent());	
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.CONFIGURATION_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(
				@PathVariable("tenant-url") String tenant,
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_CREATE
			);
		
		ModelAndView mav = new ModelAndView(Navigation.CONFIGURATION_NEW.getPath());
		
		Configuration configuration = new Configuration();
		
		configuration.setCreatedBy(configurationService.getAuthenticatedUser());
		configuration.setCreationDate(new Date());
		
		configuration.setLastUpdatedBy(configurationService.getAuthenticatedUser());
		configuration.setLastUpdateDate(new Date());
		
		configuration.setTenant(getDBTenant(tenant));
		
		mav.addObject("configuration", configuration);
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.CONFIGURATION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_EDIT 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.CONFIGURATION_EDIT.getPath());
		
		Configuration configuration = configurationService.find(getDBTenant(tenant), id);
		
		configuration.setLastUpdatedBy(configurationService.getAuthenticatedUser());
		configuration.setLastUpdateDate(new Date());
		
		mav.addObject("configuration", configuration);
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.CONFIGURATION_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(
				@PathVariable("tenant-url") String tenant,
				@PathVariable Long id
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_REMOVE 
			);
		
		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		Configuration configuration = configurationService.find(getDBTenant(tenant), id);
		
		if (configuration != null) {
			try {
				configurationService.remove(configuration);
				
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
	
	@SecuredEnum(UserPermission.CONFIGURATION_REMOVE)
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_REMOVE 
			);
		
		ModelAndView mav = new ModelAndView(Navigation.CONFIGURATION_LIST.getPath());
		
		Configuration configuration = configurationService.find(getDBTenant(tenant), id);
		
		if (configuration != null) {
			try {
				configurationService.remove(configuration);

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
			UserPermission.CONFIGURATION_VIEW, 
			UserPermission.CONFIGURATION_LIST
		})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.CONFIGURATION_VIEW, 
				UserPermission.CONFIGURATION_LIST
			);
		
		ModelAndView mav = new ModelAndView(Navigation.CONFIGURATION_VIEW.getPath());
		
		Configuration configuration = configurationService.find(getDBTenant(tenant), id);
		
		mav.addObject("configuration", configuration);
		mav.addObject("readOnly", true);
		
		return mav;
	}
	
    private boolean entityHasErrors(Tenant tenant, Configuration entity) {
		boolean hasErrors = false;
		
		if (entity != null) {
			if (validateField(tenant, entity)) {
				hasErrors = true;
			}
		}
		
		return hasErrors;
	}
    
    private String additionalValidation(Tenant tenant, Configuration entity) {
		StringBuilder message = new StringBuilder();
		
		if (entity != null) {
			if (validateField(tenant, entity)) {
				message.append(MessageBundle.getMessageBundle("configuration.field") + ": " + MessageBundle.getMessageBundle("configuration.field.duplicate") + "\n <br />");
			}
		}
		
		return message.toString();
	}
    
    private boolean validateField(Tenant tenant, Configuration entity) {
    	boolean hasError = false;
    	
    	Configuration result = configurationService.find(tenant);
		
		if(result != null 
				&& !result.getId().equals(entity.getId())){
			hasError = true;
		}
		
		return hasError;
    }

}
