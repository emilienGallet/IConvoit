package fr.iconvoit.controller;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		m.addAttribute("asList", true);
		return "planning";
	}
	
	@RequestMapping(path = {"/adding an event"},method = RequestMethod.POST)
	public String addSlotOther(
			@ModelAttribute(value="slotOther") @Validated SlotOther s,
			@ModelAttribute(value="year") @Validated Integer year,
			@ModelAttribute(value="month") @Validated Integer month,
			@ModelAttribute(value="dayOfMonth") @Validated Integer dayOfMonth,
			@ModelAttribute(value="hour") @Validated Integer hour,
			@ModelAttribute(value="minute") @Validated Integer minute,
			@ModelAttribute(value="endyear") @Validated Integer endyear,
			@ModelAttribute(value="endmonth") @Validated Integer endmonth,
			@ModelAttribute(value="enddayOfMonth") @Validated Integer enddayOfMonth,
			@ModelAttribute(value="endhour") @Validated Integer endhour,
			@ModelAttribute(value="endminute") @Validated Integer endminute) {
		s.setStart(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
		s.setEnd(LocalDateTime.of(endyear, endmonth, enddayOfMonth, endhour, endminute));
		planning.save(s);
		return "redirect:/planning";
	}
	
	@RequestMapping(path = {"/adding an event"},method = RequestMethod.GET)
	public String addSlotOther(Model m) {
		LocalDateTime start = LocalDateTime.now().plusMinutes(15);
		m.addAttribute("slotOther",new SlotOther());
		m.addAttribute("dateYear", start.getYear());
		m.addAttribute("dateMonth", start.getMonth().getValue());
		m.addAttribute("dateDayOfMonth", start.getDayOfMonth());
		m.addAttribute("dateHour", start.getHour());
		m.addAttribute("dateMinute", start.getMinute());
		start =start.plusHours(1);
		m.addAttribute("enddateYear", start.getYear());
		m.addAttribute("enddateMonth", start.getMonth().getValue());
		m.addAttribute("enddateDayOfMonth", start.getDayOfMonth());
		m.addAttribute("enddateHour", start.getHour());
		m.addAttribute("enddateMinute", start.getMinute());
		return "addEvent";
	}
}
