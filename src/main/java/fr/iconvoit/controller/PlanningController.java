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

	@RequestMapping(path = { "/my planning" })
	public String planning(final Model m) {

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
		m.addAttribute("slotTravel", new SlotTravel());// TODO POST fist submit, use vue.js
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
		m.addAttribute("slotTravel", new SlotTravel());// TODO POST fist submit, use vue.js
		m.addAttribute("slotOther", new SlotOther());// TODO Same as Up
		m.addAttribute("asList", true);
		return "planning";
	}

	@RequestMapping(path = { "/adding an event" }, method = RequestMethod.POST)
	public String addSlotOther(@ModelAttribute(value = "slotOther") @Validated final SlotOther s,
			@ModelAttribute(value = "year") @Validated final Integer year,
			@ModelAttribute(value = "month") @Validated final Integer month,
			@ModelAttribute(value = "dayOfMonth") @Validated final Integer dayOfMonth,
			@ModelAttribute(value = "hour") @Validated final Integer hour,
			@ModelAttribute(value = "minute") @Validated final Integer minute,
			@ModelAttribute(value = "endyear") @Validated final Integer endyear,
			@ModelAttribute(value = "endmonth") @Validated final Integer endmonth,
			@ModelAttribute(value = "enddayOfMonth") @Validated final Integer enddayOfMonth,
			@ModelAttribute(value = "endhour") @Validated final Integer endhour,
			@ModelAttribute(value = "endminute") @Validated final Integer endminute,
			@ModelAttribute(value = "me") @Validated final Boolean me) {
		// TODO Check if not already exist an slot across start and ending time.
		// TODO adding the Localization place for the SlotOther in template and check it
		// here.
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
		if (null == p) {
			return "redirect:/";
		}

		/* Check if Start time is before the end time */
		if (hour <= endhour) {
			return "redirect:/";
		}

		/* check slot other */
		if(!s.checkSlot()){
			return "redirect:/";
		}

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
	/*
	@RequestMapping("Slot")
	public Boolean checkSlot( Model m) {
		 Boolean answer = false;
		 String r = " SELECT * FROM Slot WHERE start = param_start AND end = param_end ";

		return answer;
	}
	*/
}
