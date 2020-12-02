package fr.iconvoit.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.iconvoit.Graph;
import fr.iconvoit.entity.Car;
import fr.iconvoit.entity.Localization;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.SlotOther;
import fr.iconvoit.entity.SlotTravel;
import fr.iconvoit.factory.LocalizationFactory;
import fr.iconvoit.factory.PathFactory;
import fr.iconvoit.factory.PeopleFactory;
import fr.iconvoit.factory.SlotFactory;
import fr.iconvoit.factory.SlotTravelFactory;
import fr.iconvoit.graphHopper.Path;

/**
 * @author Emilien , Jérémy
 */
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
	@Inject
	SlotTravelFactory slotTravelFactory;

	/**
	 * @author Emilien
	 */
	@RequestMapping(path = { "/adding an travel" }, method = RequestMethod.GET)
	public String noTravel(Model m) {
		//get user info
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user = peopleDetailsService.findByUsername(userD.getUsername());
		//if the list of ways is empty don't display the list
		//In theory i should do an service. However, no time is available.
		ArrayList<SlotTravel> travelList = new ArrayList<>();
		ArrayList<Object> tmp;
			tmp = slotTravelFactory.findIdOfSlotTravelByCar(user.getId());
			//tmp = slotTravelFactory.findSlotTravel(car);//I can improve that by personalize query. No time
			//travelList.addAll(tmp);
		
		/* TODO DELETE THIS */
		if (!user.getWays().isEmpty()) {
			m.addAttribute("ways", user.getWays());
		}/*-------------------------------*/
		m.addAttribute("ways", user.getWays());
		//pre filled fields
		LocalDateTime start = LocalDateTime.now().plusMinutes(15);
		m.addAttribute("slotOther", new SlotOther());
		m.addAttribute("dateYear", start.getYear());
		m.addAttribute("dateMonth", start.getMonth().getValue());
		m.addAttribute("dateDayOfMonth", start.getDayOfMonth());
		m.addAttribute("dateHour", start.getHour());
		m.addAttribute("dateMinute", start.getMinute());
		return "travel";
	}

	/**
	 * @author Emilien , Jérémy
	 */
	@RequestMapping(path = { "/adding an travel" }, method = RequestMethod.POST)
	public String addTravel(Model m, @ModelAttribute(value = "year") @Validated Integer year,
			@ModelAttribute(value = "month") @Validated Integer month,
			@ModelAttribute(value = "dayOfMonth") @Validated Integer dayOfMonth,
			@ModelAttribute(value = "hour") @Validated Integer hour,
			@ModelAttribute(value = "minute") @Validated Integer minute, @ModelAttribute("startLon") float startLon,
			@ModelAttribute("startLat") float startLat, @ModelAttribute("endLon") float endLon,
			@ModelAttribute("endLat") float endLat, @ModelAttribute("trajectName") String trajectName,
			@ModelAttribute("carSelect") Car carSelect) {

		//get user info
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user = peopleDetailsService.findByUsername(userD.getUsername());
		//TODO if user is null
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

		SlotTravel slotTravel = new SlotTravel();
		slotTravel.setSlotName(trajectName);
		slotTravel.setStart(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
		slotTravel.setEnd(slotTravel.getStart().plusMinutes(15));
		slotTravel.getPaths().add(p);
		slotTravel.setStartPlace(start);
		slotTravel.setFinishPlace(end);
		slotTravel.getParticipants().add(user);
		slotTravel.setCar(carSelect);

		user.getReserved().add(slotTravel);
		sf.save(slotTravel);
		pef.save(user);

		LocalDateTime startD = LocalDateTime.now().plusMinutes(15);
		m.addAttribute("slotOther", new SlotOther());
		m.addAttribute("dateYear", startD.getYear());
		m.addAttribute("dateMonth", startD.getMonth().getValue());
		m.addAttribute("dateDayOfMonth", startD.getDayOfMonth());
		m.addAttribute("dateHour", startD.getHour());
		m.addAttribute("dateMinute", startD.getMinute());

		return "travel";
	}

	/**
	 * @author Emilien , Jérémy
	 */
	@RequestMapping("/findTravel")
	public String findTravel(Model m) {
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user = peopleDetailsService.findByUsername(userD.getUsername());

		ArrayList<SlotTravel> travelList = new ArrayList<>();

		travelList = (ArrayList<SlotTravel>) slotTravelFactory.findAll();
		travelList.removeAll(user.getSlotTravel());

		m.addAttribute("travel", travelList);

		return "/findTravel";
	}

	/**
	 * @author Emilien , Jérémy
	 */
	@RequestMapping(path = "/findTravel", method = RequestMethod.POST)
	public String joinTravel(Model m, @ModelAttribute("idSlot") String idSlot) {
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user = peopleDetailsService.findByUsername(userD.getUsername());

		Optional<SlotTravel> optSlotTravel = slotTravelFactory.findById(Long.parseLong(idSlot));
		SlotTravel slotTravel = optSlotTravel.get();

		slotTravel.getParticipants().add(user);
		user.getReserved().add(slotTravel);

		sf.save(slotTravel);
		pef.save(user);

		return "redirect:/findTravel";
	}

}
