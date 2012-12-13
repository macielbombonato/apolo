package br.apolo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.apolo.web.enums.Navigation;

@Controller
public class IndexController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return Navigation.INDEX.getPath();
	}
}
