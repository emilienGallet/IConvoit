package fr.iconvoit.entity;


import org.springframework.security.core.GrantedAuthority;

/**
 * @author Jérémy Goutelle
 */

public enum PeopleRole implements GrantedAuthority  {
    USER,ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
