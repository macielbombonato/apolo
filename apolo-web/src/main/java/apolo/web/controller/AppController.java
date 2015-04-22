package apolo.web.controller;

import apolo.business.model.FileContent;
import apolo.business.model.InstallFormModel;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.util.MessageBundle;
import apolo.common.util.ThreadLocalContextUtil;
import apolo.data.enums.Language;
import apolo.data.model.User;
import apolo.security.SecuredEnum;
import apolo.security.UserPermission;
import apolo.web.enums.Navigation;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController extends BaseController<User> {
	
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private ApplicationProperties applicationProperties;
		
	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	private AuthController authController;
	
	/**
	 * System installation (setup) page
	 * @param model
	 * @param request
	 * @return ModelAndView
	 */
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/install", method = RequestMethod.GET)
	public ModelAndView install(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.INSTALL_NEW.getPath());
		
		if (hasSystemAdministrator()) {
			mav = userController.index(
					applicationProperties.getDefaultTenant(), 
					request
				);
			mav.addObject("error", true);
			mav.addObject("message", MessageBundle.getMessageBundle("install.msg.error.system.installed"));
		} else {
			InstallFormModel installFormModel = new InstallFormModel();
			mav.addObject("install", installFormModel);
		}
		
		return mav;
	}
	
	/**
	 * Save installation settings
	 * @param install
	 * @return ModelAndView
	 */
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/install/save", method = RequestMethod.POST)
	public ModelAndView save(
				@ModelAttribute("install") InstallFormModel install, 
				Model model, 
				HttpServletRequest request
			) {
		
		boolean success = false;
		
		ModelAndView mav = new ModelAndView();
		
		if (hasSystemAdministrator()) {
			mav = userController.index(
					applicationProperties.getDefaultTenant(), 
					request
				);
			
			mav.addObject("error", true);
			mav.addObject("message", MessageBundle.getMessageBundle("install.msg.error.system.installed"));
		} else {
			MultipartFile objectFile = null;
			
			List<MultipartFile> files = null;
			
			if (install != null
					&& install.getUser() != null
					&& install.getUser().getPicturefiles() != null) {
				files = install.getUser().getPicturefiles();
			}
				
			if(files != null && !files.isEmpty()) {
				for (MultipartFile multipartFile : files) {
					objectFile = multipartFile;
				}
			}
			
			if (objectFile != null 
					&& objectFile.getOriginalFilename() != null 
					&& !objectFile.getOriginalFilename().isEmpty()) {
				install.getUser().setPictureOriginalName(objectFile.getOriginalFilename());
				install.getUser().setPictureGeneratedName(objectFile.getOriginalFilename());
			}
			
			if (install != null) {
				
				if (install.getUser() != null) {
					FileContent file = null;
					
					if (objectFile != null) {
						file = new FileContent();
						file.setFile(objectFile);
					}
					
					try {
						success = userService.systemSetup(install, file);
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
					}				
				}
				
				if (success) {
					mav.addObject("msg", true);
					mav.addObject("message", MessageBundle.getMessageBundle("install.msg.success"));				
				} else {
					mav.addObject("error", true);
					mav.addObject("message", MessageBundle.getMessageBundle("install.msg.error"));
				}

			}
		}
		
		mav = authController.login(null, request);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.ADMIN)
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public ModelAndView version(HttpServletRequest request) {
		validatePermissions(UserPermission.ADMIN);
		
		ModelAndView mav = new ModelAndView(Navigation.VERSION.getPath());
		
		mav.addObject("readOnly", true);
		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.INDEX.getPath());
		
		mav.addObject("readOnly", true);
		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant-url}", method = RequestMethod.GET)
	public ModelAndView indexTenant(@PathVariable("tenant-url") String tenant, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.INDEX.getPath());

		if (getDBTenant(tenant) == null) {
			String message = MessageBundle.getMessageBundle("tenant.not.found");
			throw new AccessDeniedException(message);
		}

		mav.addObject("readOnly", true);
		return mav;
	}

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request) {
		return index(request);
	}
	
	/**
	 * Verify if the system has an administrator in the database 
	 * @return boolean
	 */
	private boolean hasSystemAdministrator() {
		boolean result = false;
		
		User systemAdmin = userService.getSystemAdministrator();
		if (systemAdmin != null) {
			result = true;
		}
		
		return result;
	}
}
