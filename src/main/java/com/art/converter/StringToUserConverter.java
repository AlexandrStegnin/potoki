package com.art.converter;

import com.art.model.Users;
import com.art.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToUserConverter implements Converter<String, Users> {

    //@Autowired
    @Resource(name = "userService")
    private UserService userService;

    @Autowired
    public StringToUserConverter(UserService userService) {
        this.userService = userService;
    }

    public Users convert(String id) {
        Users user = null;
        try{
            BigInteger IntId = new BigInteger(id);
            user = userService.findById(IntId);
        }catch(Exception ex){
            user = userService.findByLogin(id);
        }

        System.out.println("User : " + user);
        return user;
    }
}
