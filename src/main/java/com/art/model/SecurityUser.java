package com.art.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser extends Users implements UserDetails {

    public SecurityUser(Users user) {
        if (user != null) {
            this.setId(user.getId());
            this.setLogin(user.getLogin());
            this.setPassword(user.getPassword());
            this.setLastName(user.getLastName());
            this.setFirst_name(user.getFirst_name());
            this.setMiddle_name(user.getMiddle_name());
            this.setEmail(user.getEmail());
//            this.setState(user.getState());
            this.setRoles(user.getRoles());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<Roles> userRoles = this.getRoles();

        if (userRoles != null) {
            for (Roles role : userRoles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
