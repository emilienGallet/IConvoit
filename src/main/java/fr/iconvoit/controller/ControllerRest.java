package fr.iconvoit.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.iconvoit.ChangePassResponse;
import fr.iconvoit.DataCreatePath;
import fr.iconvoit.FormChangePassVue;
import fr.iconvoit.Graph;
import fr.iconvoit.IcsParser;
import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.Localization;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotOther;
import fr.iconvoit.entity.SlotTravel;
import fr.iconvoit.exceptions.SlotException;
import fr.iconvoit.factory.PeopleFactory;
import fr.iconvoit.factory.SlotFactory;
import fr.iconvoit.factory.SlotTravelFactory;
import fr.iconvoit.graphHopper.Path;

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
    public People userConected(){
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new People(userD.getUsername(),null,null);
    }

    @RequestMapping("/createPath")
    @ResponseBody
    public Path createPath(@RequestBody DataCreatePath path) {
        Localization start = new Localization("start", path.getStartLat(),path.getStartLon());
		Localization end = new Localization("end", path.getEndLat(), path.getEndLon());
        Path p = Graph.planTraject(start, end, null);
        System.err.println(p.getPoints().size());
       return p;
    }

	

	/**
	 * Reserch an travel who the use can joint it. Return to the vueJS API
	 * 
	 * @return
	 * @throws SlotException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
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

	/**
	 * Find participant of an Travel an return it to vueJS
	 * @author Émilien
	 * @param s ID of the travel
	 * @return
	 */
	@RequestMapping("/findParticipant")
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

	/**
     * Permit to join an travel by this id
     * @author Émilien
     * @param s an slotTravel ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/joinTravel")
    @ResponseBody
    public ArrayList<Object> joinTravel(@RequestBody Long s) {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        People p = listP.findByUsername(userD.getUsername());
        ArrayList<Object> list = new ArrayList<Object>();
        System.err.println("JE JOIN LE TRAVEL "+s+" "+p.getId());
        System.err.println("=> "+listSlots.joinSlot(s,p.getId()).toString());
        BigInteger b = listSlots.joinSlot(s,p.getId());
        if (b.intValueExact()>0) {
            System.out.println("WE CAN JOIN");
            listTravels.join(s,p.getId());
        }else {
            System.out.println("WE CAN'T JOIN");
        }
        //listSlots.joinSlot(s,userConected().getId());
        return list;
    }

 
    
    @RequestMapping("/addslot")
    @ResponseBody
    public void addslot(@RequestBody SlotOther slot){
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People p = peopleDetailsService.findByUsername(userD.getUsername());
        System.err.println(slot);
        System.err.println(slot.getSlotName());
        System.err.println(slot.getStart());
        System.err.println(slot.getEnd());
        System.err.println(slot.getParticipants());
        slot.getParticipants().add(p);
    	listSlots.save(slot);
        return;
    }


    @RequestMapping(path = {"/addslotIcs"},method = RequestMethod.POST)
    @ResponseBody
	public void addSlotFromUrl(@RequestBody String url) {
        url = url.substring(1, url.length()-1);

		if (url.isEmpty() || url == null) {
			
			//TODO redirect to an non valid link
			return;
        }
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People user =  peopleDetailsService.findByUsername(userD.getUsername());
		
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
			return;
		}
		//TODO Link with the People
		//
		for (Slot s : sl) {
			user.getReserved().add(s);
			s.getParticipants().add(user);
			listSlots.save(s);
		}
				
		//TODO Send success display
		return;
	}



    @PostMapping("/changePass")
    @ResponseBody
    public ChangePassResponse changePassword(@RequestBody FormChangePassVue formData) {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        People user = peopleDetailsService.findByUsername(userD.getUsername());

        ChangePassResponse rep = new ChangePassResponse(false);
        if (peopleDetailsService.bCryptPasswordEncoder.matches(formData.getOldPass(), user.getPassword()) == false) {
            return rep;
        }
        user.setPassword(formData.getNewPass());
        peopleDetailsService.save(user);
        rep.setSucces(true);
        return rep;
    }

  
}
