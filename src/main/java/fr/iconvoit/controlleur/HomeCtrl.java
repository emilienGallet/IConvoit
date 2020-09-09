package fr.iconvoit.controlleur;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeCtrl {
	@RequestMapping(path = {"/","/home"})
	public String index() {
		//TODO: process PUT request
		
		return "index";
	}

}
