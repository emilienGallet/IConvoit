package fr.iconvoit.controller;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.iconvoit.entity.SlotOther;
import fr.iconvoit.entity.SlotTravel;
import fr.iconvoit.factory.SlotFactory;


@Controller
public class PlanningController {
	@Inject
	SlotFactory planning;
	@RequestMapping(path = {"/planning"})
	public String planning(Model m) {
		m.addAttribute("planning",planning.findAll());
		m.addAttribute("slotTravel",new SlotTravel());//TODO POST fist submit, use vue.js 
		m.addAttribute("slotOther",new SlotOther());//TODO Same as Up
		return "planning";
	}
	
	@RequestMapping(path = {"/adding an event"},method = RequestMethod.POST)
	public String addSlotOther(SlotOther s) {
		planning.save(s);
		return "redirect:/planning";
	}
	
	@RequestMapping(path = {"/adding an event"},method = RequestMethod.GET)
	public String addSlotOther(Model m) {
		m.addAttribute("slotOther",new SlotOther());
		//m.addAttribute("start", );
		return "addEvent";
	}
}
