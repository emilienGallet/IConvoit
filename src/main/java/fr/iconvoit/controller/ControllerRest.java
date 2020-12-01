package fr.iconvoit.controller;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.Localization;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotTravel;
import fr.iconvoit.exceptions.SlotException;
import fr.iconvoit.factory.PeopleFactory;
import fr.iconvoit.factory.SlotFactory;
import fr.iconvoit.factory.SlotTravelFactory;

/**
 * @author Jérémy
 */
@RestController
public class ControllerRest {
	@Inject
	PeopleDetailsService peopleDetailsService;

	@Inject
	CarRepository carRep;

	@Inject
	PeopleFactory listP;

	@Inject
	SlotTravelFactory listTravels;

	@Inject
	SlotFactory listSlots;

	@RequestMapping("/user")
	@ResponseBody
	public People userConected() {
		System.err.println("userConected");
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.err.println(userD);

		// People p = peopleDetailsService.findByUsername(userD.getUsername());
		// System.err.println(p);

		return new People(userD.getUsername(), null, null);
	}

	@RequestMapping("/loadFindTravels")
	@ResponseBody
	public ArrayList<SlotTravel> findTravels() throws SlotException, IllegalArgumentException, IllegalAccessException {
		UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People p = listP.findByUsername(userD.getUsername());
		System.err.println("ID : " + p.getId());
		ArrayList<Object> l = listTravels.findTravelsOfOthers(p.getId());
		ArrayList<SlotTravel> stl = new ArrayList<>();
		for (Object object : l) {
			Object[] tab = (Object[]) object;
			// SLOT.ID,SLOT_NAME,START,END, FINISH_PLACE_ID, START_PLACE_ID
			BigInteger b = (BigInteger) tab[0];
			Long id = b.longValueExact();// object.getClass();// lf[0].get(object);
			String slotName = (String) tab[1];// lf[1].get(object);
			Timestamp TtoLdt = (Timestamp) tab[2];// lf[2].get(object);
			LocalDateTime start = TtoLdt.toLocalDateTime();
			TtoLdt = (Timestamp) tab[3];// lf[3].get(object);
			LocalDateTime end = TtoLdt.toLocalDateTime();
			/*
			 * b = (BigInteger) tab[4]; Long finishPlaceId =
			 * b.longValueExact();//lf[4].get(object); b = (BigInteger) tab[5]; Long
			 * startPlaceId = b.longValueExact();//lf[5].get(object);
			 */
			stl.add(new SlotTravel(id, slotName, start, end));
		}
		return stl;
	}

	@RequestMapping("/findOwner")
	@ResponseBody
	public ArrayList<People> findParticipants(@RequestBody Long s) {
		// ArrayList<Long> ll = listSlots.findParticipant(s);
		ArrayList<People> lp;
		ArrayList<Object> ll;
		try {
			ll = listSlots.findParticipant(s);
			// ID,USERNAME,FIRSTNAME,NAME,ID_SOURCE
			lp = new ArrayList<People>();
			for (Object object : ll) {
				Object[] tab = (Object[]) object;
				BigInteger b = (BigInteger) tab[0];
				Long id = b.longValueExact();
				String username = (String) tab[1];
				String firstName = (String) tab[2];
				String name = (String) tab[3];
				lp.add(new People(id, username, firstName, name));
			}
		} catch (Exception e) {
			// TODO: handle exception
			lp = new ArrayList<People>();
			System.err.println("Exception");
		}
		System.err.println(s);
		// ArrayList<People> lp = (ArrayList<People>) listP.findAllById(p);
		return lp;
	}

}
