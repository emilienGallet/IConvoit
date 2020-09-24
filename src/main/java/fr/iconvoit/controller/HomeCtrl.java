package fr.iconvoit.controller;

import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class HomeCtrl {
	@RequestMapping(path = {"/","/home"})
	public String index() {
		
		return "index";
	}

}
