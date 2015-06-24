package apolo.web.controller;

import apolo.business.model.FileContent;
import apolo.business.service.FileService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.util.MessageBundle;
import apolo.data.enums.Skin;
import apolo.data.enums.Spinner;
import apolo.data.enums.Status;
import apolo.data.model.Tenant;
import apolo.data.model.User;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/tenant")
public class TenantController extends BaseController<Tenant> {

	private final String ACCEPTED_FILE_TYPE = ".gif.jpg.png";
	
	@Autowired
	private TenantService tenantService;
	
	@Autowired
	private FileService<User> fileService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserController userController;
	
	@SecuredEnum(UserPermission.TENANT_MANAGER)
	@RequestMapping(value = "change/{tenant-url}", method = RequestMethod.GET)
	public ModelAndView changeTenant(
				@PathVariable("tenant-url") String tenantUrl, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_MANAGER
			);
		
		Tenant tenant = getDBTenant(tenantUrl);
		
		User user = userService.getAuthenticatedUser();
		
		user.setDbTenant(
				userService.find(user.getId()).getTenant()
			);
		
		user.setTenant(tenant);
		
        reconstructAuthenticatedUser(user);
        
        ModelAndView mav = userController.index(tenant.getUrl(), request);
		
		return mav;
	}


	@SecuredEnum(UserPermission.TENANT_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(HttpServletRequest request) {
		validatePermissions(
				UserPermission.TENANT_CREATE
			);
		
		ModelAndView mav = new ModelAndView(Navigation.TENANT_NEW.getPath());
		
		Tenant tenant = new Tenant();
		
		tenant.setStatus(Status.ACTIVE);
		tenant.setCreatedBy(tenantService.getAuthenticatedUser());
		tenant.setCreatedAt(new Date());
		
		tenant.setUpdatedBy(tenantService.getAuthenticatedUser());
		tenant.setUpdatedAt(new Date());
		
		mav.addObject("tenant", tenant);
		mav.addObject("skinList", Skin.values());
        mav.addObject("spinnerList", Spinner.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.TENANT_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id, HttpServletRequest request) {
		validatePermissions(
				UserPermission.TENANT_EDIT
			);
		
		ModelAndView mav = new ModelAndView(Navigation.TENANT_EDIT.getPath());
		
		Tenant tenant = tenantService.find(null, id);
		
		tenant.setUpdatedBy(tenantService.getAuthenticatedUser());
		tenant.setUpdatedAt(new Date());
		
		mav.addObject("tenant", tenant);
		mav.addObject("skinList", Skin.values());
        mav.addObject("spinnerList", Spinner.values());
		mav.addObject("readOnly", false);
		mav.addObject("editing", true);
		
		return mav;
	}

	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST, 
			UserPermission.TENANT_MANAGER
		})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST, 
				UserPermission.TENANT_MANAGER
			);
		
		ModelAndView mav = new ModelAndView(Navigation.TENANT_VIEW.getPath());

		Tenant tenant = tenantService.find(null, id);
		
		mav.addObject("tenant", tenant);
		mav.addObject("skinList", Skin.values());
        mav.addObject("spinnerList", Spinner.values());
		mav.addObject("readOnly", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.TENANT_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(@PathVariable Long id) {
		validatePermissions(
				UserPermission.TENANT_REMOVE
			);
		
		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		Tenant tenant = tenantService.find(null, id);
		
		if (tenant != null) {
			try {
				tenantService.remove(tenant);
				
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
	
	@SecuredEnum(UserPermission.TENANT_REMOVE)
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_REMOVE
			);
		
		ModelAndView mav = new ModelAndView(Navigation.TENANT_LIST.getPath());
		
		Tenant tenant = tenantService.find(null, id);
		
		if (tenant != null) {
			try {
				tenantService.remove(tenant);

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
	
	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST
		})
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(
				@Valid @ModelAttribute("tenant") Tenant entity, 
				BindingResult result, 
				HttpServletRequest request 
			) {
		
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST 
			);
		
		ModelAndView mav = new ModelAndView();
		
		MultipartFile logoFileToUpload = null;

		List<MultipartFile> logoFileList = null;
		
		if (entity.getLogoFile() != null) {
			logoFileList = entity.getLogoFile();
		}
		
		if(logoFileList != null && !logoFileList.isEmpty()) {
			for (MultipartFile multipartFile : logoFileList) {
				if (multipartFile != null
						&& multipartFile.getOriginalFilename() != null
						&& !multipartFile.getOriginalFilename().isEmpty()) {
					logoFileToUpload = multipartFile;
				}
			}
		}

		if (logoFileToUpload != null
				&& logoFileToUpload.getOriginalFilename() != null
				&& !logoFileToUpload.getOriginalFilename().isEmpty()) {
			entity.setLogo(logoFileToUpload.getOriginalFilename());
		}


        MultipartFile iconFileToUpload = null;

        List<MultipartFile> iconFileList = null;

        if (entity.getIconFile() != null) {
            iconFileList = entity.getIconFile();
        }

        if(iconFileList != null && !iconFileList.isEmpty()) {
            for (MultipartFile multipartFile : iconFileList) {
                if (multipartFile != null
                        && multipartFile.getOriginalFilename() != null
                        && !multipartFile.getOriginalFilename().isEmpty()) {
                    iconFileToUpload = multipartFile;
                }
            }
        }

        if (iconFileToUpload != null
                && iconFileToUpload.getOriginalFilename() != null
                && !iconFileToUpload.getOriginalFilename().isEmpty()) {
            entity.setIcon(iconFileToUpload.getOriginalFilename());
        }

		/*
		 * Object validation
		 */
		if (result.hasErrors() || entityHasErrors(entity)) {
			mav.setViewName(
					getRedirectionPath(
							null, 
							request, 
							Navigation.TENANT_NEW,
							Navigation.TENANT_EDIT
						)
				);
			mav.addObject("tenant", entity);
			mav.addObject("skinList", Skin.values());
            mav.addObject("spinnerList", Spinner.values());
			mav.addObject("readOnly", false);
			mav.addObject("error", true);
			
			/*
			 * especific validation to show or not the password field
			 */
			String referer = request.getHeader("referer");
			if (referer != null && referer.contains(Navigation.TENANT_EDIT.getPath())) {
				mav.addObject("editing", true);
			}
			
			StringBuilder message = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
				
				message.append(MessageBundle.getMessageBundle("common.field") + " " + MessageBundle.getMessageBundle("tenant." + argument.getDefaultMessage()) + ": " + error.getDefaultMessage() + "\n <br />");
			}
			
			message.append(additionalValidation(entity));
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (entity != null) {
			FileContent logo = null;
			if (logoFileToUpload != null) {
				logo = new FileContent();
				logo.setFile(logoFileToUpload);
			}

            FileContent icon = null;
            if (iconFileToUpload != null) {
                icon = new FileContent();
                icon.setFile(iconFileToUpload);
            }
			
			tenantService.save(entity, logo, icon);
			
			if (tenantService.getAuthenticatedUser().getTenant().getId().equals(entity.getId())) {
				tenantService.getAuthenticatedUser().setTenant(entity);
				
				reconstructAuthenticatedUser(tenantService.getAuthenticatedUser());
			}
			
			
			mav = view(entity.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));				
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.TENANT_MANAGER)
	@RequestMapping(value = "lock/{id}", method = RequestMethod.GET)
	public ModelAndView lock(
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_MANAGER
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_INDEX.getPath());
		
		Tenant tenant = tenantService.find(null, id);
		
		if (tenant != null) {
			
			tenantService.lock(tenant);
			
			mav = view(tenant.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.TENANT_MANAGER)
	@RequestMapping(value = "unlock/{id}", method = RequestMethod.GET)
	public ModelAndView unlock(
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_MANAGER
			);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_EDIT.getPath());
		
		Tenant tenant = tenantService.find(null, id);
		
		if (tenant != null) {
			
			tenantService.unlock(tenant);
			
			mav = view(tenant.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST, 
			UserPermission.TENANT_MANAGER
		})
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request) {
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST, 
				UserPermission.TENANT_MANAGER
			);
		
		return list(1, request);
	}
	
	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST, 
			UserPermission.TENANT_MANAGER
		})
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST, 
				UserPermission.TENANT_MANAGER
			);
		
		ModelAndView mav = new ModelAndView(Navigation.TENANT_LIST.getPath());
		
		Page<Tenant> page = tenantService.list(pageNumber);
		
		configurePageable(null, mav, page, "/tenant/list");
		
		mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("tenantList", page.getContent());	
		}
		
		return mav;
	}
	
	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST, 
			UserPermission.TENANT_MANAGER
		})
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(
				@ModelAttribute("searchParameter") String searchParameter, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST, 
				UserPermission.TENANT_MANAGER
			);
		
		return search(1, searchParameter, request);
	}
	
	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST, 
			UserPermission.TENANT_MANAGER
		})
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST, 
				UserPermission.TENANT_MANAGER
			);
		
		return search(pageNumber, "", request);
	}
	
	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST, 
			UserPermission.TENANT_MANAGER
		})
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable Integer pageNumber, 
				@PathVariable String searchParameter, 
				HttpServletRequest request
			) {
		
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST, 
				UserPermission.TENANT_MANAGER
			);
		
		ModelAndView mav = new ModelAndView(Navigation.TENANT_LIST.getPath());
		
		Page<Tenant> page = tenantService.search(pageNumber, searchParameter);
		
		String url = "";
		
		if (searchParameter == null || "".equalsIgnoreCase(searchParameter)) {
			url = "/tenant/search";
		} else {
			url = "/tenant/search/"+searchParameter;
		}
		
		configurePageable("", mav, page, url);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("tenantList", page.getContent());	
		}
		
		return mav;
	}
	
	@SecuredEnum({
			UserPermission.TENANT_EDIT, 
			UserPermission.TENANT_CREATE, 
			UserPermission.TENANT_LIST, 
			UserPermission.TENANT_MANAGER
		})
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(HttpServletRequest request) {
		validatePermissions(
				UserPermission.TENANT_EDIT, 
				UserPermission.TENANT_CREATE, 
				UserPermission.TENANT_LIST, 
				UserPermission.TENANT_MANAGER
			);
		
		ModelAndView mav = new ModelAndView(Navigation.TENANT_SEARCH.getPath());
		
		return mav;
	}
	
    private boolean entityHasErrors(Tenant entity) {
		boolean hasErrors = false;
		
		if (entity != null) {
			if (isValidFileType(entity)) {
				hasErrors = true;
			}
		}
		
		return hasErrors;
	}
    
    private String additionalValidation(Tenant entity) {
		StringBuilder message = new StringBuilder();
		
		if (entity != null) {
			if (isValidFileType(entity)) {
				message.append(MessageBundle.getMessageBundle("tenant.logo") + ": " + MessageBundle.getMessageBundle("tenant.fileType") + "\n <br />");
			}
		}
		
		return message.toString();
	}
    
	private boolean isValidFileType(Tenant entity) {
		boolean hasErrors = false;
		
		if (entity.getLogo() != null
				&& entity.getLogo().length() > 0
				&& !ACCEPTED_FILE_TYPE.contains(fileService.extractFileExtension(entity.getLogo()))
				) {
			hasErrors = true;
		}
		
		return hasErrors;
	}

}
