package com.art.converter;

import com.art.model.AppUser;
import com.art.service.UserService;
import org.springframework.core.convert.converter.Converter;

public class UserConverter implements Converter<String, AppUser> {

    private final UserService userService;

    public UserConverter(UserService userService) {
        this.userService = userService;
    }

    public AppUser convert(String id) {
        AppUser user;
        if (id.equalsIgnoreCase("0")) {
            return null;
        }
        try {
            Long IntId = Long.valueOf(id);
            user = userService.findById(IntId);
        } catch (Exception ex) {
            user = userService.findByLogin(id);
        }
        return user;
    }
}
