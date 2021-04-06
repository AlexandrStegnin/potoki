package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.config.application.Location;
import com.art.model.supporting.dto.BalanceDTO;
import com.art.service.AccountTransactionService;
import com.art.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Objects;

@Controller
public class AppController {

    private final UserService userService;

    private final AccountTransactionService accountTransactionService;

    public AppController(UserService userService, AccountTransactionService accountTransactionService) {
        this.userService = userService;
        this.accountTransactionService = accountTransactionService;
    }

    @GetMapping(path = { Location.HOME, Location.WELCOME, Location.INVESTMENTS})
    public String welcomePage(SecurityContextHolderAwareRequestWrapper request, ModelMap model) {
        userService.confirm(SecurityUtils.getUserId());
        if (request.isUserInRole("ROLE_INVESTOR") && !request.isUserInRole("ROLE_ADMIN")) {
            Long ownerId = SecurityUtils.getUserId();
            BalanceDTO balanceDTO = accountTransactionService.getBalance(ownerId);
            BigDecimal balance = balanceDTO.getSummary();
            if (balance.compareTo(BigDecimal.valueOf(-1)) > 0 && balance.compareTo(BigDecimal.ONE) < 0) {
                balance = BigDecimal.ZERO;
            }
            model.addAttribute("ownerId", ownerId);
            model.addAttribute("balance", balance);
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
            model.addObject("error", "Invalid username and password!");
        }
        if (logout != null) {
            model.addObject("msg", "Вы вышли из системы.");
        }
        model.setViewName("login");
        return model;
    }

    @GetMapping(path = Location.LOGOUT)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
            }

        }
        return "redirect:/login?logout";
    }

    @GetMapping(path = Location.DEMO)
    public String demoPage(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && !(auth instanceof AnonymousAuthenticationToken)) {
            SecurityContextHolder.clearContext();
        }
        return "redirect:" + Location.LOGIN;
    }

}

