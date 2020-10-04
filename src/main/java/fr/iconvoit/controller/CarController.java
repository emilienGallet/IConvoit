package fr.iconvoit.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.Car;
import fr.iconvoit.entity.CarRepository;

@RequestMapping("/IConvoit")
@Controller
public class CarController {
    @Inject
    public CarRepository carRep;
    

    @RequestMapping(path = {"/car"})
	public String Car(Model m) {
        m.addAttribute("carL", carRep.findAll());
        m.addAttribute("addCar", new Car());
		return "car";
    }
    
    @RequestMapping(path = {"addcar"})
	public String AddCar(Car c) {
        carRep.save(c);
        return "redirect:car";
    }
}
