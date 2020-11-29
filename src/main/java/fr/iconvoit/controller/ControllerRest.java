package fr.iconvoit.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public People userConected(){
        System.err.println("userConected");
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.err.println(userD);

       // People p = peopleDetailsService.findByUsername(userD.getUsername());
    //    System.err.println(p);

        
        return new People(userD.getUsername(),null,null);
    }
    
    @RequestMapping("/loadFindTravels")
    @ResponseBody
    public ArrayList<SlotTravel> findTravels() throws SlotException{
    	UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	People p = listP.findByUsername(userD.getUsername());
    	System.err.println("ID : "+ p.getId());
    	ArrayList<SlotTravel> l =listTravels.findTravelsOfOthers(p.getId());
    	//A supprimer
    	l = new ArrayList<>();
    	SlotTravel st = new SlotTravel("TTTT", "jhgfc", "kjhvg",null,null);
    	st.setId(1L);
    	st.setStart(LocalDateTime.now());
    	st.setEnd(LocalDateTime.now().plusMinutes(15));
    	l.add(st);
    	
    	SlotTravel sa = new SlotTravel("UUUUUUU", "jhgfc", "kjhvg",null,null);
    	sa.setId(2L);
    	sa.setStart(LocalDateTime.now());
    	sa.setEnd(LocalDateTime.now().plusMinutes(15));
    	l.add(sa);
    	////
    	System.err.println(p.getId());
    	l =listTravels.findTravelsOfOthers(p.getId());
    	//l.get(0).setPaths(null);
    	//l.get(1).setPaths(null);
    	if (!l.isEmpty()) {			
    		l.get(0).setParticipants(null);
    		l.get(1).setParticipants(null);
		}
    	return l;
    }
    @RequestMapping("/findOwner")
    @ResponseBody
    public ArrayList<People> findParticipants(@RequestBody Long s) throws SlotException{
    	ArrayList<Long> ll = listSlots.findParticipant(s);
    	System.err.println(s);
    	//ArrayList<People> lp = (ArrayList<People>) listP.findAllById(p);
    	ArrayList<People> lp = new ArrayList<>();
    	for (Long l : ll) {
			//TODO Faire en sorte de retrouver les personnes par leurs ID
		}
    	return lp;
    }
    
    
}
