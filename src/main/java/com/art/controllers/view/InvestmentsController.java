package com.art.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class InvestmentsController {

    @GetMapping(path = "/investments")
    public String showInvestments() {
        return "/flows";
    }

}
