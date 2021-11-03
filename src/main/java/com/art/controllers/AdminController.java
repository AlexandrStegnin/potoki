package com.art.controllers;

import com.art.config.application.Location;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

  @Secured("ROLE_ADMIN")
  @GetMapping(path = Location.CATALOGUE)
  public String cataloguePage(ModelMap model) {
    return "catalogues";
  }

}
