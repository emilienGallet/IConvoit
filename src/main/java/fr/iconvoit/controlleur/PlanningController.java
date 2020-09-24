package fr.iconvoit.controlleur;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.iconvoit.entity.PlanningRepository;

@Controller
public class PlanningController {
	//@Inject
	//PlanningRepository p;
	@RequestMapping(path = {"/planning"})
	public String planning() {
		
		return "planning";
	}
}
