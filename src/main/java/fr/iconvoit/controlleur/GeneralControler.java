package fr.iconvoit.controlleur;

import org.springframework.stereotype.Controller;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralControler extends SpringServletContainerInitializer {

	public GeneralControler() {
		// TODO Auto-generated constructor stub
		
	}
	
	@GetMapping("/home")
	public String home() {
		return "index";
	}
}
