package fr.iconvoit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TravelController {

	@RequestMapping(path = {"/adding an travel"}, method = RequestMethod.GET)
	public String planning(Model m) {
		
		
		return "travel";
	}
}
