package com.art.config;

import com.art.model.AppUser;
import com.art.model.SecurityUser;
import com.art.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource(name = "userService")
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        AppUser user = userService.findByLoginWithAnnexes(login);
        if (Objects.isNull(user) || user.getProfile().isLocked()) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new SecurityUser(user);
    }

}
