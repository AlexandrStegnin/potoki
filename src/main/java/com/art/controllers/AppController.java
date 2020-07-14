package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.func.GetPrincipalFunc;
import com.art.model.AppUser;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.ServiceTemporarilyUnavailableException;
import com.art.model.supporting.ServiceUnavailable;
import com.art.service.ServiceUnavailableService;
import com.art.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class AppController {

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "serviceUnavailableService")
    private ServiceUnavailableService serviceUnavailableService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/switch", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse checkServiceStatus(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        String activate = "";
        switch (searchSummary.getSwitchSite()) {
            case "1":
                activate = "Сайт отключен";
                break;
            case "0":
                activate = "Сайт включен";
                break;
        }
        ServiceUnavailable serviceUnavailable = serviceUnavailableService.findServiceUnavailable();
        try {
            serviceUnavailable.setStatus(Integer.parseInt(searchSummary.getSwitchSite()));
            serviceUnavailableService.update(serviceUnavailable);
            response.setMessage(activate);
        } catch (Exception ex) {
            response.setError("Ошибка. Сайт не отключен");
        }
        return response;
    }

    @GetMapping(value = {"/", "/investments", "/welcome"})
    public String welcomePage(SecurityContextHolderAwareRequestWrapper request, ModelMap model) {
        boolean admin = request.isUserInRole("ROLE_ADMIN");
        if (serviceUnavailableService.findServiceUnavailable().getStatus() == 1 && !admin) {
            throw new ServiceTemporarilyUnavailableException("Сайт временно не доступен");
        }

        if (request.isUserInRole("ROLE_INVESTOR") &&
                (!admin && !request.isUserInRole("ROLE_DBA") && !request.isUserInRole("ROLE_BIGDADDY"))) {
            userService.confirm(getPrincipalFunc.getPrincipalId());
            model.addAttribute("investorLogin", SecurityUtils.getUsername());
            return "flows";
        } else {
            return "catalogues";
        }

    }


    @RequestMapping(value = "/Access_Denied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        return "accessDenied";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
            String targetUrl = getRememberMeTargetUrlFromSession(request);
            if (StringUtils.hasText(targetUrl)) {
                model.addObject("targetUrl", targetUrl);
                model.addObject("loginUpdate", true);
            }
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

    /**
     * get targetURL from session
     */
    private String getRememberMeTargetUrlFromSession(HttpServletRequest request) {
        String targetUrl = "";
        HttpSession session = request.getSession(false);
        if (session != null) {
            targetUrl = session.getAttribute("targetUrl") == null ? ""
                    : session.getAttribute("targetUrl").toString();
        }
        return targetUrl;
    }

    @GetMapping(value = "/demo")
    public String demoPage(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && !(auth instanceof AnonymousAuthenticationToken)) {
            SecurityContextHolder.clearContext();
        }
        AppUser demo = userService.findByLogin(SecurityUtils.getInvestorDemoLogin());
        String login;
        if (Objects.isNull(demo)) {
            login = SecurityUtils.getUsername();
        } else {
            login = demo.getLogin();
        }
        model.addAttribute("investorLogin", login);
        return "flows";
    }

}

