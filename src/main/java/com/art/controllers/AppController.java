package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.config.application.Location;
import com.art.model.supporting.dto.BalanceDTO;
import com.art.service.AccountService;
import com.art.service.AccountTransactionService;
import com.art.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppController {

  UserService userService;
  AccountService accountService;
  AccountTransactionService accountTransactionService;

  @GetMapping(path = {Location.HOME, Location.WELCOME, Location.INVESTMENTS})
  public String welcomePage(SecurityContextHolderAwareRequestWrapper request, ModelMap model) {
    if (request.isUserInRole("ROLE_INVESTOR") && !request.isUserInRole("ROLE_ADMIN")) {
      Long userId = SecurityUtils.getUserId();
      userService.confirm(userId);
      BalanceDTO balanceDTO = accountTransactionService.getBalanceByInvestorLogin(SecurityUtils.getUsername());
      model.addAttribute("ownerId", balanceDTO.getAccountId());
      model.addAttribute("balance", balanceDTO.getSummary());
      model.addAttribute("investorLogin", SecurityUtils.getUsername());
      return "flows";
    } else {
      return "catalogues";
    }
  }

  @RequestMapping(value = "/Access_Denied")
  public String accessDeniedPage(ModelMap model) {
    model.addAttribute("loggedinuser", SecurityUtils.getUsername());
    return "accessDenied";
  }

  @GetMapping(path = Location.LOGIN)
  public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            HttpServletRequest request) {
    ModelAndView model = new ModelAndView();
    if (error != null) {
      model.addObject("error", "Неверные имя пользователя и/или пароль!");
    }
    if (logout != null) {
      model.addObject("msg", "Вы вышли из системы.");
    }
    model.setViewName("login");
    return model;
  }

}

