package br.apolo.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.model.FileContent;
import br.apolo.business.model.InstallFormModel;
import br.apolo.business.service.UserService;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.model.User;
import br.apolo.web.enums.Navigation;
import br.apolo.web.service.BreadCrumbTreeService;

@Controller
public class IndexController {
	
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private BreadCrumbTreeService breadCrumbService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * System welcome page
	 * @param model
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.home"), 0, request);
		return Navigation.INDEX.getPath();
	}
	
	/**
	 * System installation (setup) page
	 * @param model
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/install", method = RequestMethod.GET)
	public ModelAndView install(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.INSTALL_NEW.getPath());
		
		if (hasSystemAdministrator()) {
			mav.setViewName(Navigation.INDEX.getPath());
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
	@RequestMapping(value = "/install/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("install") InstallFormModel install) {
		boolean success = false;
		
		ModelAndView mav = new ModelAndView();
		
		if (hasSystemAdministrator()) {
			mav.setViewName(Navigation.INDEX.getPath());
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
				
				mav.setViewName(Navigation.INDEX.getPath());
				
				if (success) {
					mav.addObject("msg", true);
					mav.addObject("message", MessageBundle.getMessageBundle("install.msg.success"));				
				} else {
					mav.addObject("error", true);
					mav.addObject("message", MessageBundle.getMessageBundle("install.msg.error"));
				}

			}
		}
		
		return mav;
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
