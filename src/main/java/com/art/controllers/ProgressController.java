package com.art.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class ProgressController {

    @MessageMapping("/status")
    @SendTo("/progress/status")
    public String greeting(String message) {
        return message;
    }

}
