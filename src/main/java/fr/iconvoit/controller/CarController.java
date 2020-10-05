package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.iconvoit.entity.Car;
import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;

/**
 * @author Mélanie , Jérémy
 */

@Controller
public class CarController {
    @Inject
    CarRepository carRep;

    @Inject
    PeopleDetailsService peopleDetailsService;


    /**
     * @author Mélanie
     */
    @RequestMapping("/car")
    public String Car(Model m) {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        People p = peopleDetailsService.findByUsername(userD.getUsername());
        m.addAttribute("user", p);
        m.addAttribute("cars", p.getMyCars());
        m.addAttribute("addCar", new Car());
        return "/car";
    }

     /**
     * @author jérémy
     */
    @RequestMapping(path = "/addcar", method = RequestMethod.POST)
    public String AddCar(Car c , @ModelAttribute("nbSeats") int nbSeats) {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        People p = peopleDetailsService.findByUsername(userD.getUsername());

        if(c.verifRegistration(c.getRegistration()) == false){
            return "/car";
        }
        c.setNbOfSeats(nbSeats);
        c.setOwner(p);
        p.addCar(c);
        
        carRep.save(c);
        return "redirect:/car";
    }

     /**
     * @author Jérémy
     */
    @RequestMapping(path = "/suppCar", method = RequestMethod.POST)
    public String SuppCar(@ModelAttribute("idCar") String idCar) {
    
        carRep.deleteById(Long.parseLong(idCar));

        return "redirect:/car";

    }

}
