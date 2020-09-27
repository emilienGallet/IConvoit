package fr.iconvoit.controller;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.SlotOther;
import fr.iconvoit.entity.SlotTravel;
import fr.iconvoit.factory.PeopleFactory;
import fr.iconvoit.factory.SlotFactory;



@Controller
/**
 * 
 * @author Émilien
 * Planning management.
 * Ability to add an event and to display planning of the current User
 *
 */
public class PlanningController{
	
	@Inject
	PeopleDetailsService peopleDetailsService;
	
	@RequestMapping(path = {"/my planning"})
	public String planning(Model m) {
		
		/*
		 * Get info from the current user.
		 */
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People p = peopleDetailsService.findByUsername(userD.getUsername());
		if (p==null) {
			return "redirect:/" ;
		}
		m.addAttribute("planning",p.getReserved());
		m.addAttribute("slotTravel",new SlotTravel());//TODO POST fist submit, use vue.js 
		m.addAttribute("slotOther",new SlotOther());//TODO Same as Up
		m.addAttribute("asList", true);
		return "planning";
	}
	@Inject
	SlotFactory planning;
	@RequestMapping(path = {"/all planning"})
	public String toutLesPlanning(Model m) {
		
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
			@ModelAttribute(value="endminute") @Validated Integer endminute,
			@ModelAttribute(value="me") @Validated Boolean me) {
		s.setStart(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
		s.setEnd(LocalDateTime.of(endyear, endmonth, enddayOfMonth, endhour, endminute));
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People p = peopleDetailsService.findByUsername(userD.getUsername());
		/*We checked if is an event for him and check if he was connect*/
		if (me || p!=null) {
			p.getReserved().add(s);
			s.getParticipants().add(p);
			System.out.println(p.getReserved());
			System.out.println(s.getParticipants());
		}
		if (null == p) {
			return "redirect:/";
		}
		planning.save(s);
		return "redirect:/my planning";
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
