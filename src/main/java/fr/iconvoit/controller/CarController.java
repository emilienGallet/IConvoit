package fr.iconvoit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.Car;


@Controller
public class CarController {
    Car c;
    @RequestMapping(path = {"/car"})
	public String Car(Model m) {
        m.addAttribute("carFind", c.getRegistration());
		return "car";
    }
    
    @RequestMapping(path = {"/addcar"})
	public String AddCar(Model m) {
		return "redirect:/car";
    }
}
