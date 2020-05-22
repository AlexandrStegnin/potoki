package com.art.controllers.view;

import com.art.model.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class InvestmentsController {

    @PostMapping(path = "/investments")
    public String showInvestments(@ModelAttribute Users user, ModelMap model) {
        model.addAttribute("investorLogin", user.getLogin());
        return "/flows";
    }

}
