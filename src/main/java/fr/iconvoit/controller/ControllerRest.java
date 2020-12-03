package fr.iconvoit.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.iconvoit.IcsParser;
import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
import fr.iconvoit.entity.Slot;
import fr.iconvoit.entity.SlotOther;
import fr.iconvoit.factory.SlotFactory;

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
    SlotFactory slotrep;


    @RequestMapping("/user")
    @ResponseBody
    public People userConected(){
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        return new People(userD.getUsername(),null,null);
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
        slotrep.save(slot);
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
			slotrep.save(s);
		}
				
		//TODO Send success display
		return;
	}


}
