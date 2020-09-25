package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.SlotTravel;
import fr.iconvoit.factory.SlotFactory;


@Controller
public class PlanningController {
	@Inject
	SlotFactory planning;
	@RequestMapping(path = {"/planning"})
	public String planning(Model m) {
		m.addAttribute("planning",planning.findAll());
		m.addAttribute("slotTravel",new SlotTravel());
		return "planning";
	}
}
