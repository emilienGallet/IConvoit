package fr.iconvoit.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.iconvoit.IcsParser;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotOther;
import fr.iconvoit.entity.SlotTravel;
import fr.iconvoit.factory.PeopleFactory;
import fr.iconvoit.factory.SlotFactory;

@Controller
/**
 * 
 * @author Émilien Planning management. Ability to add an event and to display
 *         planning of the current User
 *
 */
public class PlanningController {

	@Inject
	PeopleDetailsService peopleDetailsService;
	
	@RequestMapping(path = {"/my planning"})
	//@ModelAttribute(value="url");
	public String planning(Model m) {
		
		/*
		 * Get info from the current user.
		 */
		final UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final People p = peopleDetailsService.findByUsername(userD.getUsername());
		if (p == null) {
			return "redirect:/";
		}
		/*
		 * Adding attributes for the thymeleaf
		 */
		m.addAttribute("planning", p.getReserved());// Sending the List<Slot> reserved by the "p" people
		m.addAttribute("slotTravel", new SlotTravel());// TODO POST first submit, use vue.js
		m.addAttribute("slotOther", new SlotOther());// TODO Same as Up
		m.addAttribute("asList", true);// Boolean attribute who's permit to display list<Slot> as list or like
										// traditional planning
		return "planning";
	}

	@Inject
	SlotFactory planning;

	@RequestMapping(path = { "/all planning" })
	public String toutLesPlanning(final Model m) {

		m.addAttribute("planning", planning.findAll());
		m.addAttribute("slotTravel", new SlotTravel());// TODO POST first submit, use vue.js
		m.addAttribute("slotOther", new SlotOther());// TODO Same as Up
		m.addAttribute("asList", true);
		return "planning";
	}
	@Inject
	PeopleFactory peopleL;
	@RequestMapping(path = {"/adding from url"},method = RequestMethod.POST)
	public String addSlotFromUrl(@ModelAttribute(value="url") @Validated String url,RedirectAttributes redirect) {
		if (url.isEmpty() || url == null) {
			
			//TODO redirect to an non valid link
			return "redirect:/adding from url";
		}
		People user = getUserInfo();
		
		peopleL.findById(user.getId());
		ArrayList<Slot> sl = null;
		try {
			sl = IcsParser.parsing(url);
		} catch (MalformedURLException e) {
			// TODO URL unreachable
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Reading error
			e.printStackTrace();
		}
		if (sl == null) {
			//TODO send error display
			return "redirect:/url error";
		}
		//TODO Link with the People
		//
		for (Slot s : sl) {
			user.getReserved().add(s);
			s.getParticipants().add(user);
			planning.save(s);
		}
		//user.setReserved(sl);
		//peopleL.save(user);
		
		//TODO Send success display
		return "redirect:/my planning";
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
		//TODO Check if start time is before the end time.
		//TODO Check if not already exist an slot across start and ending time.
		//TODO adding the Localization place for the SlotOther in template and check it here.
		s.setStart(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
		s.setEnd(LocalDateTime.of(endyear, endmonth, enddayOfMonth, endhour, endminute));
		final UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final People p = peopleDetailsService.findByUsername(userD.getUsername());
		/* We checked if is an event for him and check if he was connect */
		if (me || p != null) {
			p.getReserved().add(s);
			s.getParticipants().add(p);
			System.out.println(p.getReserved());
			System.out.println(s.getParticipants());
		}
		System.err.println(hour);
		System.err.println(endhour);
		

		/* Check if Start time is before the end time */
		if (hour >= endhour) {
			return "redirect:/error";
		}

		/* check slot other */
		/*if(!s.checkSlot()){
			return "redirect:/";
		}
*/
		planning.save(s);
		return "redirect:/my planning";
	}

	@RequestMapping(path = { "/adding an event" }, method = RequestMethod.GET)
	public String addSlotOther( Model m) {
		// TODO adding the Localization place for the SlotOther in template
		LocalDateTime start = LocalDateTime.now().plusMinutes(15);
		m.addAttribute("slotOther", new SlotOther());
		m.addAttribute("dateYear", start.getYear());
		m.addAttribute("dateMonth", start.getMonth().getValue());
		m.addAttribute("dateDayOfMonth", start.getDayOfMonth());
		m.addAttribute("dateHour", start.getHour());
		m.addAttribute("dateMinute", start.getMinute());
		start = start.plusHours(1);
		m.addAttribute("enddateYear", start.getYear());
		m.addAttribute("enddateMonth", start.getMonth().getValue());
		m.addAttribute("enddateDayOfMonth", start.getDayOfMonth());
		m.addAttribute("enddateHour", start.getHour());
		m.addAttribute("enddateMinute", start.getMinute());
		return "addEvent";
	}
	
	private People getUserInfo() {
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return peopleDetailsService.findByUsername(userD.getUsername());	
	}

	
}
