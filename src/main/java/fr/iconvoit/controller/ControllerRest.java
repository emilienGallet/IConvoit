package fr.iconvoit.controller;

import javax.inject.Inject;

import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.iconvoit.ChangePassResponse;
import fr.iconvoit.DataCreatePath;
import fr.iconvoit.FormChangePassVue;
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
    public People userConected() {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       
        return new People(userD.getUsername(), null, null);
    }

    @PostMapping("/changePass")
    @ResponseBody
    public ChangePassResponse changePassword(@RequestBody FormChangePassVue formData) {
        UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        People user = peopleDetailsService.findByUsername(userD.getUsername());

        ChangePassResponse rep = new ChangePassResponse(false);
        if (peopleDetailsService.bCryptPasswordEncoder.matches(formData.getOldPass(), user.getPassword()) == false) {
            return rep;
        }
        user.setPassword(formData.getNewPass());
        peopleDetailsService.save(user);
        rep.setSucces(true);
        return rep;
    }

  
}
