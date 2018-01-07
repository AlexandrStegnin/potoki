package com.art.config.application.testPackage;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

public class testClass {
    private static final Locale RU = new Locale("ru", "RU");
    public static void main(String[] args) throws Exception {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123"));

    }

}
