package fr.iconvoit.entity;


import org.springframework.security.core.GrantedAuthority;

public enum PeopleRole implements GrantedAuthority  {
    USER,ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
