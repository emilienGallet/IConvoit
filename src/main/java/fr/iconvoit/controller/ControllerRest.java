package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.iconvoit.DataCreatePath;
import fr.iconvoit.Graph;
import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.Localization;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;
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


    @RequestMapping("/user")
    @ResponseBody
    public People userConected(){
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new People(userD.getUsername(),null,null);
    }

    @RequestMapping("/createPath")
    @ResponseBody
    public Path createPath(@RequestBody DataCreatePath path) {
        Localization start = new Localization("", path.getStartLat(),path.getEndLon());
		Localization end = new Localization("", path.getEndLat(), path.getEndLon());
        Path p = Graph.planTraject(start, end, null);
        System.err.println(p.getPoints().size());
       return p;
    }

}
