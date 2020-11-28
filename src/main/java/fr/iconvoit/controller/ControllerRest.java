package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;

/**
 * @author Jérémy
 */
@RestController
public class ControllerRest {
    @Inject
    PeopleDetailsService peopleDetailsService;
    
    @Inject
    CarRepository carRep;


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

}
