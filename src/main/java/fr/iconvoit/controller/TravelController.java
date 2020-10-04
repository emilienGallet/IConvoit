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
import fr.iconvoit.factory.LocalizationFactory;
import fr.iconvoit.factory.PathFactory;
import fr.iconvoit.factory.PeopleFactory;
import fr.iconvoit.factory.SlotFactory;
import fr.iconvoit.graphHopper.Path;

@Controller
public class TravelController {

	@Inject
	PeopleDetailsService peopleDetailsService;
	@Inject
	SlotFactory sf;
	@Inject
	LocalizationFactory lf;
	@Inject
	PathFactory pf;
	@Inject
	PeopleFactory pef;

	@RequestMapping(path = { "/adding an travel" }, method = RequestMethod.GET)
	public String noTravel(Model m) {
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user = peopleDetailsService.findByUsername(userD.getUsername());
		if (!user.getWays().isEmpty()) {
			m.addAttribute("ways", user.getWays());	
		}
		return "travel";
	}

	@RequestMapping(path = { "/adding an travel" }, method = RequestMethod.POST)
	public String addTravel(Model m, @ModelAttribute("startLon") float startLon,
			@ModelAttribute("startLat") float startLat, @ModelAttribute("endLon") float endLon,
			@ModelAttribute("endLat") float endLat,@ModelAttribute("trajectName") String trajectName) {

		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user = peopleDetailsService.findByUsername(userD.getUsername());
		Localization start = new Localization("", startLat, startLon);
		Localization end = new Localization("", endLat, endLon);
		lf.save(start);
		lf.save(end);
		Path p = Graph.planTraject(start, end, null);
		p.setName(trajectName);
		pf.save(p);
		user.getWays().add(p);
		pef.save(user);
		m.addAttribute("list", p.getPoints());
		if (!user.getWays().isEmpty()) {
			m.addAttribute("ways", user.getWays());	
		}

		return "travel";
	}

}
