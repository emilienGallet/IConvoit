package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.PeopleValidator;

/**
 * @author Jérémy
 */
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

	@RequestMapping("/vue")
	public String menuVue() {
		return "menuVue";
	}

	@GetMapping("/register")
	public String register(Model m) {

		m.addAttribute("register", new People());
		return "register";
	}

	@PostMapping("/register")
	public String addUser(@ModelAttribute("register") People p, BindingResult bindingResult) {
		peopleValidator.validate(p, bindingResult);

		if (bindingResult.hasErrors()) {
			return "/register";
		}
		peopleDetailsService.save(p);
		return "redirect:/";
	}

	@GetMapping("/profile")
	public String profile(Model m) {
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People p = peopleDetailsService.findByUsername(userD.getUsername());
		m.addAttribute("user", p);
		m.addAttribute("password", new String());
		m.addAttribute("newPassword", new String());

		return "profile";
	}

	@PostMapping("/profile")
	public String changePassword(Model m, @ModelAttribute("password") String p,
			@ModelAttribute("newPassword") String newP) {

		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user = peopleDetailsService.findByUsername(userD.getUsername());
		m.addAttribute("user", user);

		if (peopleDetailsService.bCryptPasswordEncoder.matches(p, user.getPassword()) == false) {
			m.addAttribute("fail", "boolean");

			return "profile";
		}

		user.setPassword(newP);
		peopleDetailsService.save(user);
		m.addAttribute("success", "boolean");

		return "redirect:/profile?success=true";
	}

}
