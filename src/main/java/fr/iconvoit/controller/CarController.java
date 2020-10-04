package fr.iconvoit.controller;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.Car;
import fr.iconvoit.entity.CarRepository;
import fr.iconvoit.entity.People;

// @RequestMapping("/IConvoit")
@Controller
public class CarController {
    @Inject
    public CarRepository carRep;
    @Inject
    public People people;
    
    public ArrayList<Car> carsBis=people.cars;
    @RequestMapping("/IConvoit/car")
	public String Car(Model m) {
        // m.addAttribute("carL", people.cars.get(0));
        m.addAttribute("nbCar", carsBis.size());
        m.addAttribute("addCar", new Car());
		return "car";
    }
    
    @RequestMapping("/IConvoit/addcar")
	public String AddCar(Car c) {
        carRep.save(c);
        carsBis.add(c);
        return "redirect:/car";
    }
}
