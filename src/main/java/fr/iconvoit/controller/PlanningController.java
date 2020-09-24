package fr.iconvoit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PlanningController {
	//@Inject
	//PlanningRepository p;
	@RequestMapping(path = {"/planning"})
	public String planning() {
		
		return "planning";
	}
}
