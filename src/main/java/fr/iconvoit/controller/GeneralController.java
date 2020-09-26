package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.PeopleValidator;

@Controller

public class GeneralController extends SpringServletContainerInitializer {

	@Inject
	PeopleDetailsService peopleDetailsService;

	@Inject
	PeopleValidator peopleValidator;

	@RequestMapping("")
	public String home() {
		return "index";
	}

	@GetMapping("/register")
	public String register(Model m) {

		m.addAttribute("register", new People());
		return "register";
	}

	@PostMapping("/register")
	public String addUser(People p,BindingResult bindingResult) {
		peopleValidator.validate(p, bindingResult);

		if(bindingResult.hasErrors()){
			return "redirect:/register";
		}
		
		peopleDetailsService.save(p);
		return "redirect:/";
	}


	@RequestMapping("/test")
	public String test(Model m){
		return"test";
	}

	@RequestMapping(path = "/profile")
	public String profile() {
		
		return "profile";
	}

	@RequestMapping(path = "/car")
	public String car() {
		
		return "car";
	}

	@RequestMapping(path = "/travel")
	public String travel() {
		
		return "travel";
	}
}
