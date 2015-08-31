package apolo.web.controller;

import apolo.business.model.FileContent;
import apolo.business.service.*;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.util.MessageBundle;
import apolo.data.enums.UserStatus;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.security.UserPermission;
import apolo.web.enums.Navigation;
import net.sf.json.JSONObject;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/{tenant-url}/user")
public class UserController extends BaseController<User> {

	private final String ACCEPTED_FILE_TYPE = ".gif.jpg.png";
	
	@Inject
	UserService userService;
	
	@Inject
	UserGroupService userGroupService;
	
	@Inject
	UserCustomFieldService userCustomFieldService;
	
	@Inject
	private FileService<User> fileService;
	
	@Inject
	private ApplicationProperties applicationProperties;

	@Inject
	private TenantService tenantService;

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		ModelAndView mav = new ModelAndView(Navigation.USER_INDEX.getPath());

		mav.addObject("user", userService.getAuthenticatedUser());

		double userCount = userService.count();

		double userTenantCount = userService.countByTenant(getDBTenant(tenant));
		double userTenantCountPercent = (userTenantCount / userCount) * 100;

		double tenantCount = tenantService.count();

		mav.addObject("tenantCount", tenantCount);

		mav.addObject("userCount", userCount);
		mav.addObject("userTenantCount", userTenantCount);
		mav.addObject("userTenantCountPercent", userTenantCountPercent);

		mav.addObject("readOnly", true);
		return mav;
	}

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "change-password", method = RequestMethod.GET)
	public ModelAndView changePassword(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {
		ModelAndView mav = new ModelAndView(Navigation.USER_CHANGE_PASSWORD.getPath());
		
		mav.addObject("user", userService.getAuthenticatedUser());
		mav.addObject("readOnly", true);
		mav.addObject("changePassword", true);
		return mav;
	}

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "change-password-save", method = RequestMethod.POST)
	public ModelAndView changePasswordSave(
				@PathVariable("tenant-url") String tenant,
				@ModelAttribute("user") User user, 
				HttpServletRequest request, 
				@RequestParam(defaultValue = "") String passwordConfirmation
			) {
		ModelAndView mav = index(tenant, request);
		
		if (entityHasErrors(user, true, passwordConfirmation, null)) {
			mav.setViewName(
					getRedirectionPath(
							tenant, 
							request, 
							Navigation.USER_CHANGE_PASSWORD, 
							Navigation.USER_CHANGE_PASSWORD
						)
				);
			mav.addObject("user", userService.getAuthenticatedUser());
			mav.addObject("readOnly", true);
			mav.addObject("changePassword", true);
			mav.addObject("error", true);
			
			StringBuilder message = new StringBuilder();
			message.append(additionalValidation(user, true, passwordConfirmation, null));
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (user != null) {
			User dbuser = userService.find(getDBTenant(tenant), user.getId());
			
			dbuser.setPassword(user.getPassword());
			
			userService.save(dbuser, true, null);
		}
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.hasPermission('USER_CREATE')")
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(
				@PathVariable("tenant-url") String tenantUrl, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_NEW.getPath());
		
		User user = new User();
		
		user.setStatus(UserStatus.ACTIVE);
		user.setCreatedBy(userService.getAuthenticatedUser());
		user.setCreatedAt(new Date());
		
		user.setUpdatedBy(userService.getAuthenticatedUser());
		user.setUpdatedAt(new Date());
		
		Tenant tenant = getDBTenant(tenantUrl);
		
		// The tenant language is used as a reference
		user.setTenant(tenant);
		
		mav.addObject("user", user);
		mav.addObject("groupList", userGroupService.list(tenant));
		mav.addObject("customFieldList", userCustomFieldService.list(tenant));
		mav.addObject("readOnly", false);
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		ModelAndView mav = new ModelAndView(Navigation.USER_EDIT.getPath());
		
		User user = userService.find(getDBTenant(tenant), id);
		
		// The authenticated user can change your own profile or others if has user edit permission
		if (user != null
				&& authenticatedUserHasPermission(userService, user, UserPermission.USER_EDIT, true)) {

			user.setUpdatedBy(userService.getAuthenticatedUser());
			user.setUpdatedAt(new Date());
			
			mav.addObject("user", user);
			mav.addObject("groupList", userGroupService.list(user.getTenant()));
			mav.addObject("customFieldList", userCustomFieldService.list(user.getTenant()));
			mav.addObject("readOnly", false);
			mav.addObject("editing", true);			
		} else {
			String message = MessageBundle.getMessageBundle("user.msg.error.permission");
			throw new AccessDeniedException(message);
		}
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {
		ModelAndView mav = new ModelAndView(Navigation.USER_VIEW.getPath());

		User user = null;
		
		if (userService.getAuthenticatedUser().getId().equals(id)) {
			user = userService.find(id);
			
			if (user.getDbTenant() != null) {
				user.setTenant(user.getDbTenant());				
			}
			
			reconstructAuthenticatedUser(user);
		} else { 
			user = userService.find(getDBTenant(tenant), id);
		}
		
		if (user != null
				&& authenticatedUserHasPermission(userService, user, UserPermission.USER_LIST, false)) {
			user.getPermissions();
			mav.addObject("user", user);
			mav.addObject("customFieldList", userCustomFieldService.list(user.getTenant()));
			mav.addObject("readOnly", true);
		} else {
			String message = MessageBundle.getMessageBundle("user.msg.error.permission");
			throw new AccessDeniedException(message);
		}
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_REMOVE')")
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id
			) {

		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		User user = userService.find(getDBTenant(tenant), id);
		
		if (user != null) {
			if (!userService.getAuthenticatedUser().equals(user)) {
				try {
					userService.remove(user);
					
					result = MessageBundle.getMessageBundle("common.msg.remove.success");
					jsonItem.put("success", true);
				} catch (Throwable e) {
					result = MessageBundle.getMessageBundle("common.remove.msg.error");
					jsonItem.put("success", false);
				}
			} else {
				result = MessageBundle.getMessageBundle("user.msg.error.remove.yourself");
				jsonItem.put("success", false);
			}
		}
		
		jsonItem.put("message", result);
		jsonSubject.accumulate("result", jsonItem);
		
		return jsonSubject.toString();
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_REMOVE')")
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		User user = userService.find(getDBTenant(tenant), id);
		
		if (user != null) {
			try {
				userService.remove(user);

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

	@PreAuthorize("@apoloSecurity.isAuthenticated()")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(
				@PathVariable("tenant-url") String tenant, 
				@Valid @ModelAttribute("user") User entity, 
				BindingResult result, 
				HttpServletRequest request, 
				@RequestParam(defaultValue = "false") boolean changePassword, 
				@RequestParam(defaultValue = "") String passwordConfirmation
			) {
		ModelAndView mav = new ModelAndView();
		
		MultipartFile objectFile = null;
		
		List<MultipartFile> files = null;
		
		if (entity.getPicturefiles() != null) {
			files = entity.getPicturefiles();
		}
		
		if(files != null && !files.isEmpty()) {
			for (MultipartFile multipartFile : files) {
				objectFile = multipartFile;
			}
		}
		
		if (objectFile != null 
				&& objectFile.getOriginalFilename() != null 
				&& !objectFile.getOriginalFilename().isEmpty()) {
			entity.setAvatarOriginalName(objectFile.getOriginalFilename());
			entity.setAvatarFileName(objectFile.getOriginalFilename());
		}
		
		/*
		 * Object validation
		 */
		if (result.hasErrors() || entityHasErrors(entity, changePassword, passwordConfirmation, objectFile)) {
			mav.setViewName(getRedirectionPath(tenant, request, Navigation.USER_NEW, Navigation.USER_EDIT));
			mav.addObject("user", entity);
			mav.addObject("groupList", userGroupService.list(getDBTenant(tenant)));
			mav.addObject("customFieldList", userCustomFieldService.list(getDBTenant(tenant)));
			mav.addObject("readOnly", false);
			mav.addObject("error", true);
			
			/*
			 * especific validation to show or not the password field
			 */
			String referer = request.getHeader("referer");
			if (referer != null && referer.contains(Navigation.USER_EDIT.getPath())) {
				mav.addObject("editing", true);
			}
			
			StringBuilder message = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
				
				message.append(MessageBundle.getMessageBundle("common.field") + " " + MessageBundle.getMessageBundle("user." + argument.getDefaultMessage()) + ": " + error.getDefaultMessage() + "\n <br />");
			}
			
			message.append(additionalValidation(entity, changePassword, passwordConfirmation, objectFile));
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (entity != null) {
			if (authenticatedUserHasPermission(userService, entity, UserPermission.USER_CREATE, true) 
					|| authenticatedUserHasPermission(userService, entity, UserPermission.USER_EDIT, true)) {
				FileContent file = null;
				
				if (objectFile != null) {
					file = new FileContent();
					file.setFile(objectFile);
				}
				
				userService.save(entity, changePassword, file);
				
				// Reconstruct user in session
				if (userService.getAuthenticatedUser().getId().equals(entity.getId())) {
					reconstructAuthenticatedUser(entity);
				}
				
				mav = view(tenant, entity.getId(), request);
				
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));				
			} else {
				String message = MessageBundle.getMessageBundle("user.msg.error.permission");
				throw new AccessDeniedException(message);
			}
		}
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_MANAGER')")
	@RequestMapping(value = "lock/{id}", method = RequestMethod.GET)
	public ModelAndView lock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_EDIT.getPath());
		
		User user = userService.find(getDBTenant(tenant), id);
		
		if (user != null) {
			
			userService.lock(user);
			
			mav = view(tenant, user.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_MANAGER')")
	@RequestMapping(value = "unlock/{id}", method = RequestMethod.GET)
	public ModelAndView unlock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_EDIT.getPath());
		
		User user = userService.find(getDBTenant(tenant), id);
		
		if (user != null) {
			
			userService.unlock(user);
			
			mav = view(tenant, user.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.hasPermission('USER_MANAGER')")
	@RequestMapping(value = "list-locked", method = RequestMethod.GET)
	public ModelAndView listLocked(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		return listLocked(tenant, 1, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_MANAGER')")
	@RequestMapping(value = "list-locked/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView listLocked(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		Page<User> page = userService.listLocked(getDBTenant(tenant), pageNumber);
		
		configurePageable(tenant, mav, page, "/user/list-locked");
		
		mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userList", page.getContent());	
		}
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.hasPermission('USER_LIST')")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		return list(tenant, 1, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_LIST')")
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		Page<User> page = userService.list(getDBTenant(tenant), pageNumber);
		
		configurePageable(tenant, mav, page, "/user/list");
		
		mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userList", page.getContent());	
		}
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_LIST')")
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@ModelAttribute("searchParameter") String searchParameter, 
				HttpServletRequest request
			) {

		return search(tenant, 1, searchParameter, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_LIST')")
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		return search(tenant, pageNumber, "", request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_LIST')")
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				@PathVariable String searchParameter, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		Page<User> page = userService.search(getDBTenant(tenant), pageNumber, searchParameter);
		
		String url = "";
		
		if (searchParameter == null || "".equalsIgnoreCase(searchParameter)) {
			url = "/user/search";
		} else {
			url = "/user/search/"+searchParameter;
		}
		
		configurePageable(tenant, mav, page, url);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userList", page.getContent());	
		}
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('USER_LIST')")
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.USER_SEARCH.getPath());
		
		return mav;
	}
	
    private boolean entityHasErrors(User entity, boolean changePassword, String passwordConfirmation, MultipartFile objectFile) {
		boolean hasErrors = false;
		
		if (entity != null) {
			if (validateEmail(entity)) {
				hasErrors = true;
			} else if(changePassword && !entity.getPassword().equals(passwordConfirmation)) {
				hasErrors = true;
			} else if (isValidFileType(entity, objectFile)) {
				hasErrors = true;
			}
		}
		
		return hasErrors;
	}
    
    private boolean validateEmail(User entity) {
    	boolean hasError = false;
    	
    	User result = userService.findByLogin(entity.getEmail());
		
		if(result != null 
				&& !result.getId().equals(entity.getId())){
			hasError = true;
		}
		
		return hasError;
    }
    
    private String additionalValidation(User entity, boolean changePassword, String passwordConfirmation, MultipartFile objectFile) {
		StringBuilder message = new StringBuilder();
		
		if (entity != null) {
			if (validateEmail(entity)) {
				message.append(MessageBundle.getMessageBundle("user.email") + ": " + MessageBundle.getMessageBundle("user.email.duplicate") + "\n <br />");
			}
			
			if(changePassword && !entity.getPassword().equals(passwordConfirmation)) {
				message.append(MessageBundle.getMessageBundle("user.password.confirmation") + ": " + MessageBundle.getMessageBundle("user.password.confirmatin.failure") + "\n <br />");
			}
			
			if (isValidFileType(entity, objectFile)) {
				message.append(MessageBundle.getMessageBundle("user.picturefiles") + ": " + MessageBundle.getMessageBundle("user.fileType") + "\n <br />");
			}
		}
		
		return message.toString();
	}
    
	private boolean isValidFileType(User entity, MultipartFile objectFile) {
		boolean hasErrors = false;
		
		if (objectFile != null && !objectFile.isEmpty()) {
			if (entity.getAvatarOriginalName() == null
					|| (entity.getAvatarOriginalName() != null
					&& entity.getAvatarOriginalName().length() > 0
					&& !ACCEPTED_FILE_TYPE.contains(fileService.extractFileExtension(entity.getAvatarOriginalName()))
			)) {
				hasErrors = true;
			}
		}

		return hasErrors;
	}

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "groups", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = null;

                if(element instanceof String && !((String)element).equals("")){
                    //From the JSP 'element' will be a String
                    try{
                        id = Long.parseLong((String) element);
                    } catch (NumberFormatException e) {
                        log.error("Element was " + ((String) element), e);
                    }
                } else if(element instanceof Long) {
                    //From the database 'element' will be a Long
                    id = (Long) element;
                }

                return id != null ? userGroupService.find(id) : null;
            }
          });

		super.initBinder(binder);
    }

}
