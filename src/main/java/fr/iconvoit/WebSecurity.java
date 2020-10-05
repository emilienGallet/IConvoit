package fr.iconvoit;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import fr.iconvoit.entity.PeopleDetailsService;

/**
 * Jérémy Goutelle
 */

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Inject
    PeopleDetailsService peopleDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/","/register","/css/**","/h2-console/**").permitAll()
            .anyRequest().authenticated()
            .and().formLogin().defaultSuccessUrl("/my planning",true)
            .and().logout().logoutSuccessUrl("/");
            http.csrf().disable();
        http.headers().frameOptions().disable();

    }

     @Override
     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(peopleDetailsService).passwordEncoder(peopleDetailsService.bCryptPasswordEncoder);

        auth.inMemoryAuthentication()
        .passwordEncoder(peopleDetailsService.bCryptPasswordEncoder) 
        .withUser("robert").password(peopleDetailsService.bCryptPasswordEncoder.encode("toto")).roles("USER", "ADMIN")
        .and().withUser("bob").password("toto").roles("USER");
    }
}
