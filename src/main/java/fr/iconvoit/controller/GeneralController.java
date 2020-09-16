package fr.iconvoit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController extends SpringServletContainerInitializer {

	public GeneralController() {
		// TODO Auto-generated constructor stub
		
	}
	
	@GetMapping("/home")
	public String home() {
		return "index";
	}
}
