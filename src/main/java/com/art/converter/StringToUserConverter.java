package com.art.converter;

import com.art.model.AppUser;
import com.art.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;

public class StringToUserConverter implements Converter<String, AppUser> {

    //@Autowired
    @Resource(name = "userService")
    private UserService userService;

    @Autowired
    public StringToUserConverter(UserService userService) {
        this.userService = userService;
    }

    public AppUser convert(String id) {
        AppUser user;
        try {
            Long IntId = Long.valueOf(id);
            user = userService.findById(IntId);
        } catch (Exception ex) {
            user = userService.findByLogin(id);
        }
        return user;
    }
}
