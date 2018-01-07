package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.Stuffs;
import com.art.model.Users;
import com.art.model.supporting.SearchSummary;
import com.art.service.StuffService;
import com.art.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {

    //@Autowired
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;
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

    /*
    @RequestMapping(value = "/usersByStuff", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    String usersByStuffPage(@RequestBody SearchSummary searchSummary,
                            SecurityContextHolderAwareRequestWrapper request) {
        boolean admin = request.isUserInRole("ROLE_ADMIN");
        boolean dba = request.isUserInRole("ROLE_DBA");
        BigInteger stuffId = new BigInteger(searchSummary.getSearchStuff());
        List<Users> usersList;

        if(!Objects.equals(searchSummary.getSearchStuff(), "0")){
            usersList = userService.findByStuffId(stuffId);
        }else{
            usersList = userService.findAll();
        }

        StringBuilder result;
        result = new StringBuilder("<thead><tr><th>ID</th><th>Фамилия</th><th>Имя</th>" +
                "<th>Отчество</th><th>Имя пользователя</th><th>Email</th>" +
                "<th>Признак</th><th>Статус</th>");
        if(admin || dba){
            result.append("<th>Изменить</th>");
        }
        if(admin){
            result.append("<th>Удалить</th>");
        }
        result.append("</tr></thead><tbody>");
        for (Users users : usersList) {
            result.append("<tr>")
                    .append("<td>").append(users.getId()).append("</td>")
                    .append("<td>").append(users.getLastName()).append("</td>")
                    .append("<td>").append(users.getFirst_name()).append("</td>")
                    .append("<td>").append(users.getMiddle_name()).append("</td>")
                    .append("<td>").append(users.getLogin()).append("</td>")
                    .append("<td>").append(users.getEmail()).append("</td>")
                    .append("<td>").append(users.getState()).append("</td>")
                    .append("<td>").append(users.getUserStuff().getStuff()).append("</td>");
                    if(admin || dba){
                        result.append("<td><a href='/edit-user-").append(users.getId()).append("'")
                                .append(" class='btn btn-success custom-width'>Изменить</a></td>");
                    }
                    if(admin){
                        result.append("<td><a href='/deleteuser'")
                                .append(" id='delete' name='").append(users.getId()).append("'")
                        .append(" class='btn btn-danger custom-width'>Удалить</a></td>");
                    }
                    result.append("</tr>")
                    .append("</tbody>");
        }

        return result.toString();
    }
    */

    private void setRememberMeTargetUrlToSession(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        if(session!=null){
            session.setAttribute("targetUrl", "/admin");
        }
    }

    public String getRememberMeTargetUrlFromSession(HttpServletRequest httpServletRequest){
        String targetUrl = "";
        HttpSession session = httpServletRequest.getSession(false);
        if(session!=null){
            targetUrl = session.getAttribute("targetUrl")==null?""
                    :session.getAttribute("targetUrl").toString();
        }
        return targetUrl;
    }

    private boolean isRememberMeAuthenticated() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());

    }

    @ModelAttribute("stuffs")
    public List<Stuffs> initializeStuffs() {
        return stuffService.initializeStuffs();
    }
}
