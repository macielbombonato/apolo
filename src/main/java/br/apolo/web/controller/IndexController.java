package br.apolo.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.apolo.common.util.MessageBundle;
import br.apolo.web.enums.Navigation;
import br.apolo.web.service.BreadCrumbTreeService;

@Controller
public class IndexController {
	
	@Autowired
	protected BreadCrumbTreeService breadCrumbService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.home"), 0, request);
		return Navigation.INDEX.getPath();
	}
}
