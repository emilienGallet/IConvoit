package fr.iconvoit.entity;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fr.iconvoit.factory.PeopleFactory;

/**
 * @author Jérémy Goutelle
 */

@Component
public class PeopleDetailsService implements UserDetailsService {

    @Inject
    PeopleFactory peopleList;

    public final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        People people = peopleList.findByUsername(username);
        if (people == null)
            throw new UsernameNotFoundException(username);

        return new User(people.getUsername(), people.getPassword(), people.getRoles());
    }

    public void save(People people) {
        people.setPassword(bCryptPasswordEncoder.encode(people.getPassword()));
        people.getRoles().add(PeopleRole.USER);
        peopleList.save(people);
    }

    public People findByUsername(String username) {
        return peopleList.findByUsername(username);
    }

}
