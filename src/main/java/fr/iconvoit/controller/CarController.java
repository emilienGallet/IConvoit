package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.Car;
import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.People;
import fr.iconvoit.entity.PeopleDetailsService;

@Controller
public class CarController {
    @Inject
     CarRepository carRep;
 
     @Inject
     PeopleDetailsService peopleDetailsService;
 
    // ArrayList<Car> carsBis=people.cars;

    @RequestMapping("/car")
	public String Car(Model m) {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People p = peopleDetailsService.findByUsername(userD.getUsername());
        m.addAttribute("user", p);
        
        // m.addAttribute("carL", people.cars.get(0));
       // m.addAttribute("nbCar", p.getCars().size());
       m.addAttribute("cars", p.getCars());
        m.addAttribute("addCar", new Car());
		return "car";
    }
    
    @RequestMapping("/addcar")
	public String AddCar(Car c) {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		People p = peopleDetailsService.findByUsername(userD.getUsername());
     //   p.addCar(c.getColor(), c.getBrand(),c.getRegistration(), c.getFormat(), c.getNbOfSeats());
        carRep.save(c);
   //     peopleDetailsService.save(p);
        return "redirect:/car";
    }
}
