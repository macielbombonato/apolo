package apolo.web.controller;

import apolo.business.service.ApplicationService;
import apolo.business.service.UserGroupService;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.model.Application;
import apolo.web.controller.base.BaseWebController;
import apolo.web.enums.Navigation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Set;

@Controller
@RequestMapping(value = "/web/{tenant-url}/application")
public class ApplicationWebController extends BaseWebController<Application> {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private UserGroupService userGroupService;
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_CREATE', 'APPLICATION_EDIT')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(
				@PathVariable("tenant-url") String tenant, 
				@Valid @ModelAttribute("entity") Application entity,
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
							Navigation.APPLICATION_NEW,
							Navigation.APPLICATION_EDIT
						)
				);
			
			mav.addObject("app", entity);
			mav.addObject("groupList", userGroupService.list(getDBTenant(tenant)));
			mav.addObject("readOnly", false);
			mav.addObject("error", true);
			
			StringBuilder message = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
				
				message.append(MessageBundle.getMessageBundle("common.field") + " " + MessageBundle.getMessageBundle("application." + argument.getDefaultMessage()) + ": " + error.getDefaultMessage() + "\n <br />");
			}
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (entity != null) {
			try {
				entity.setApplicationKey(null);

				applicationService.save(entity);
				
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

	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_LIST')")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		return list(tenant, 1, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_LIST')")
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_LIST.getPath());
		
		Page<Application> page = applicationService.list(getDBTenant(tenant), pageNumber);
		
	    configurePageable(tenant, mav, page, "/application/list");
	    
	    mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("appList", page.getContent());
		}
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_CREATE')")
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_NEW.getPath());
		
		Application app = new Application();
		
		app.setCreatedBy(userGroupService.getAuthenticatedUser());
		app.setCreatedAt(new Date());
		
		app.setUpdatedBy(userGroupService.getAuthenticatedUser());
		app.setUpdatedAt(new Date());
		
		app.setTenant(getDBTenant(tenant));
		
		mav.addObject("app", app);
		mav.addObject("groupList", userGroupService.list(getDBTenant(tenant)));
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_EDIT')")
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_EDIT.getPath());
		
		Application app = applicationService.find(getDBTenant(tenant), id);
		
		app.setUpdatedBy(applicationService.getAuthenticatedUser());
		app.setUpdatedAt(new Date());
		
		mav.addObject("app", app);
		mav.addObject("groupList", userGroupService.list(getDBTenant(tenant)));
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_REMOVE')")
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id
			) {

		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		Application app = applicationService.find(getDBTenant(tenant), id);
		
		if (app != null) {
			try {
				applicationService.remove(app);

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
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_REMOVE')")
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_LIST.getPath());
		
		Application app = applicationService.find(getDBTenant(tenant), id);
		
		if (app != null) {
			try {
				applicationService.remove(app);

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
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_LIST')")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_VIEW.getPath());
		
		Application app = applicationService.find(getDBTenant(tenant), id);
		
		mav.addObject("app", app);
		mav.addObject("readOnly", true);
		
		return mav;
	}

	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_LIST')")
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@ModelAttribute("searchParameter") String searchParameter, 
				HttpServletRequest request
			) {

		return search(tenant, 1, searchParameter, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_LIST')")
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		return search(tenant, pageNumber, "", request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_LIST')")
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Integer pageNumber, 
				@PathVariable String searchParameter, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_LIST.getPath());
		
		Page<Application> page = applicationService.search(getDBTenant(tenant), pageNumber, searchParameter);
		
		String url = "";
		
		if (searchParameter == null || "".equalsIgnoreCase(searchParameter)) {
			url = "/application/search";
		} else {
			url = "/application/search/"+searchParameter;
		}
		
		configurePageable(tenant, mav, page, url);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("appList", page.getContent());
		}

		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_LIST')")
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(
				@PathVariable("tenant-url") String tenant, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_SEARCH.getPath());
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_EDIT')")
	@RequestMapping(value = "lock/{id}", method = RequestMethod.GET)
	public ModelAndView lock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_VIEW.getPath());
		
		Application app = applicationService.find(getDBTenant(tenant), id);
		
		if (app != null) {
			
			applicationService.lock(app);
			
			mav = view(tenant, app.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('APPLICATION_EDIT')")
	@RequestMapping(value = "unlock/{id}", method = RequestMethod.GET)
	public ModelAndView unlock(
				@PathVariable("tenant-url") String tenant, 
				@PathVariable Long id, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.APPLICATION_VIEW.getPath());
		
		Application app = applicationService.find(getDBTenant(tenant), id);
		
		if (app != null) {
			
			applicationService.unlock(app);
			
			mav = view(tenant, app.getId(), request);
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
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
