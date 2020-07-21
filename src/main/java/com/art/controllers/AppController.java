package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.model.AppUser;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class AppController {

    private final UserService userService;

    public AppController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/investments", "/welcome"})
    public String welcomePage(SecurityContextHolderAwareRequestWrapper request, ModelMap model) {
        boolean admin = request.isUserInRole("ROLE_ADMIN");
        userService.confirm(SecurityUtils.getUserId());
        if (request.isUserInRole("ROLE_INVESTOR") &&
                (!admin && !request.isUserInRole("ROLE_DBA") && !request.isUserInRole("ROLE_BIGDADDY"))) {
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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
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

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
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

    @GetMapping(value = "/demo")
    public String demoPage(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && !(auth instanceof AnonymousAuthenticationToken)) {
            SecurityContextHolder.clearContext();
        }
        AppUser demo = userService.findByLogin(SecurityUtils.getInvestorDemoLogin());
        String login;
        if (null == demo) {
            login = SecurityUtils.getUsername();
        } else {
            login = demo.getLogin();
        }
        model.addAttribute("investorLogin", login);
        return "flows";
    }

}

