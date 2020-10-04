package fr.iconvoit.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.iconvoit.Graph;
import fr.iconvoit.entity.Localization;

@Controller
public class TravelController {

	@RequestMapping(path = {"/adding an travel"}, method = RequestMethod.GET)
	public String noTravel(Model m) {
		return "travel";
	}
	
	@RequestMapping(path = {"/adding an travel"}, method = RequestMethod.POST)
	public String noTravel() {
		return "travel";
	}
}
