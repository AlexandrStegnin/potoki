package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.Stuffs;
import com.art.model.Users;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.InvestorsFlowsService;
import com.art.service.StuffService;
import com.art.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    /**
     * This update page is for user login with password only.
     * If user is login via remember me cookie, send login to ask for password again.
     * To avoid stolen remember me cookie to update info
     */

    @Secured({"ROLE_ADMIN", "ROLE_DBA"})
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(ModelMap model, HttpServletRequest httpServletRequest) {

        List<Users> users = userService.findAllWithStuffs();
        model.addAttribute("users", users);
        model.addAttribute("searchSummary", new SearchSummary());

        if (isRememberMeAuthenticated()) {
            setRememberMeTargetUrlToSession(httpServletRequest);
            model.addAttribute("loginUpdate", true);
        }
        return "admin";
    }

    @Secured({"ADMIN", "DBA"})
    @GetMapping(value = "/catalogue")
    public String cataloguePage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        return "/catalogues";
    }

    @PostMapping(value = {"/updateInvestorDemo"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateInvestorDemo() {
        GenericResponse response = new GenericResponse();
        investorsFlowsService.updateInvestorDemo();
        response.setMessage("Данные инвестора демо успешно обновлены");
        return response;
    }

    private void setRememberMeTargetUrlToSession(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.setAttribute("targetUrl", "/admin");
        }
    }

    private boolean isRememberMeAuthenticated() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());

    }

    @ModelAttribute("stuffs")
    public List<Stuffs> initializeStuffs() {
        List<Stuffs> stuffs = stuffService.initializeStuffs();
        Stuffs confirmed = new Stuffs("-1", "Не подтверждён");
        stuffs.add(confirmed);
        return stuffs;
    }
}
