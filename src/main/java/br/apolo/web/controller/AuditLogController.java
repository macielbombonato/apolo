package br.apolo.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.service.AuditLogService;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.enums.UserPermission;
import br.apolo.data.model.AuditLog;
import br.apolo.security.SecuredEnum;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/auditlog")
public class AuditLogController extends BaseController<AuditLog> {

	@Autowired
	AuditLogService auditLogService;
	
	@Override
	@SecuredEnum(UserPermission.ADMIN)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request) {
		return list(1, request);
	}

	@Override
	@SecuredEnum(UserPermission.ADMIN)
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable Integer pageNumber, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.auditlog.list"), 1, request);
		
		ModelAndView mav = new ModelAndView(Navigation.AUDIT_LOG_LIST.getPath());
		
		Page<AuditLog> page = auditLogService.list(pageNumber);
		
		configurePageable(mav, page, "/auditlog/list");
		
		mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("auditList", page.getContent());	
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.ADMIN)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(@ModelAttribute("searchParameter") String searchParameter, HttpServletRequest request) {
		return search(1, searchParameter, request);
	}
	
	@SecuredEnum(UserPermission.ADMIN)
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(@PathVariable Integer pageNumber, @PathVariable String searchParameter, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.user.list"), 2, request);
		
		ModelAndView mav = new ModelAndView(Navigation.AUDIT_LOG_LIST.getPath());
		
		Page<AuditLog> page = auditLogService.search(pageNumber, searchParameter);
		
		configurePageable(mav, page, "/auditlog/search/"+searchParameter);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("auditList", page.getContent());
		}
		
		return mav;
	}

	@Override
	public ModelAndView save(AuditLog entity, BindingResult result,
			HttpServletRequest request) {
		return null;
	}

	@Override
	public ModelAndView create(HttpServletRequest request) {
		return null;
	}

	@Override
	public ModelAndView edit(Long id, HttpServletRequest request) {
		return null;
	}

	@Override
	public String remove(Long id) {
		return null;
	}

}
