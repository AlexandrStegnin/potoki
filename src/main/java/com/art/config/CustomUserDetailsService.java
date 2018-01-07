package com.art.config;

import com.art.model.SecurityUser;
import com.art.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    //get user from the database, via Hibernate
    //@Autowired
    @Resource(name = "userService")
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        com.art.model.Users user = userService.findByLoginWithAnnexes(login);
        //System.out.println("User : "+user);
        if(user==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new SecurityUser(user);
    }

}
