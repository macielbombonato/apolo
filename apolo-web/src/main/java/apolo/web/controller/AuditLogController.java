package apolo.web.controller;

import apolo.business.service.AuditLogService;
import apolo.data.model.AuditLog;
import apolo.web.enums.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/{tenant}/auditlog")
public class AuditLogController extends BaseController<AuditLog> {

	@Autowired
	private AuditLogService auditLogService;
	
	@PreAuthorize("@apoloSecurity.hasPermission('ADMIN')")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("tenant") String tenant, HttpServletRequest request) {

		return list(tenant, 1, request);
	}

	@PreAuthorize("@apoloSecurity.hasPermission('ADMIN')")
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(
				@PathVariable("tenant") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.AUDIT_LOG_LIST.getPath());
		
		Page<AuditLog> page = auditLogService.list(pageNumber);
		
		configurePageable(tenant, mav, page, "/auditlog/list");
		
		mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("auditList", page.getContent());	
		}
		
		return mav;
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('ADMIN')")
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(
				@PathVariable("tenant") String tenant, 
				@ModelAttribute("searchParameter") String searchParameter, 
				HttpServletRequest request
			) {

		return search(tenant, 1, searchParameter, request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('ADMIN')")
	@RequestMapping(value = "search/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant") String tenant, 
				@PathVariable Integer pageNumber, 
				HttpServletRequest request
			) {

		return search(tenant, pageNumber, "", request);
	}
	
	@PreAuthorize("@apoloSecurity.hasPermission('ADMIN')")
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(
				@PathVariable("tenant") String tenant, 
				@PathVariable Integer pageNumber, 
				@PathVariable String searchParameter, 
				HttpServletRequest request
			) {

		ModelAndView mav = new ModelAndView(Navigation.AUDIT_LOG_LIST.getPath());
		
		Page<AuditLog> page = auditLogService.search(pageNumber, searchParameter);
		
		String url = "";
		
		if (searchParameter == null || "".equalsIgnoreCase(searchParameter)) {
			url = "/auditlog/search";
		} else {
			url = "/auditlog/search/"+searchParameter;
		}
		
		configurePageable(tenant, mav, page, url);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("auditList", page.getContent());
		}
		
		return mav;
	}
}
