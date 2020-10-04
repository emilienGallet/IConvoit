package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.iconvoit.Graph;
import fr.iconvoit.entity.Localization;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.SlotTravel;

@Controller
public class TravelController {

	@Inject
	PeopleDetailsService peopleDetailsService;


	@RequestMapping(path = { "/adding an travel" }, method = RequestMethod.GET)
	public String noTravel(Model m) {

		return "travel";
	}

	@RequestMapping(path = { "/adding an travel" }, method = RequestMethod.POST)
	public String addTravel(Model m,@ModelAttribute("startLon") float startLon, @ModelAttribute("startLat") float startLat,
			@ModelAttribute("endLon") float endLon, @ModelAttribute("endLat") float endLat) {

				UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				People user = peopleDetailsService.findByUsername(userD.getUsername());
				
				Localization start = new  Localization("", startLat, startLon);
				Localization end = new  Localization("", endLat, endLon);

				user.getMyTrajectList().add(new SlotTravel(start,end));

			//	peopleDetailsService.save(user);
				m.addAttribute("list", Graph.planTraject(start,end,null).getPoints());


		return "travel";
	}
}
