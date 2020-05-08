package com.art.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class KindController {

    @GetMapping(path = "/kind")
    public String testKind() {
        return "/flows-v2";
    }

}
