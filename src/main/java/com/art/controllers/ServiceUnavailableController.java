package com.art.controllers;

import com.art.model.supporting.Greeting;
import com.art.model.supporting.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ServiceUnavailableController {
    @MessageMapping("/turn")
    @SendTo("/support/messages")
    public Greeting greeting(HelloMessage message) {
        return new Greeting(message.getName());
    }

    @GetMapping(value = "/wspage")
    public ModelAndView wsPage(){
        return new ModelAndView("ws");
    }
}
