package com.art.controllers;

import com.art.model.PasswordResetToken;
import com.art.model.Users;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.MailService;
import com.art.model.supporting.SearchSummary;
import com.art.service.PasswordResetTokenService;
import com.art.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

@Controller
public class ResetPasswordController {

    private Properties prop = new Properties();

    @Resource
    private MailService mailService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "passwordResetTokenService")
    private PasswordResetTokenService passwordResetTokenService;

    @GetMapping(value = "/forgotPassword")
    public ModelAndView forgotPasswordPage() {
        return new ModelAndView("forgotPassword");
    }

    @PostMapping(value = "/resetPassword", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse resetPasswordPage(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        Users users;
        try {
            users = userService.findByLoginAndEmail(searchSummary.getLogin(), searchSummary.getEmail());
        } catch (Exception e) {
            response.setError("<p>Пользователь с таким email и логином не найден.</p>");
            return response;
        }

        PasswordResetToken oldToken = passwordResetTokenService.findById(users.getId());
        if (oldToken != null) {
            passwordResetTokenService.deleteById(users.getId());
        }

        String fileName = "mail.ru.properties";

        InputStream input;
        try {
            input = MailService.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String token = UUID.randomUUID().toString();
        String url = prop.getProperty("mail.urlProm") +
                users.getId() + "&token=" + token;
        PasswordResetToken resetToken = new PasswordResetToken(token, users);

        Calendar cal = new GregorianCalendar();

        resetToken.setExpiryDate(new java.sql.Date(cal.getTime().getTime()));
        passwordResetTokenService.create(resetToken);

        String body = "Уважаемый, " + users.getLogin()
                + ", восстановить пароль можно пройдя по ссылке ниже."
                + "<br>"
                + "<a href=" + url + ">Восстановить пароль</a>"
                + "<br>"
                + "Ссылка действительная в течении 24 часов."
                + "<br>"
                + "Сообщение создано автоматически, не отвечайте на него.";
        String subject = "Воссстановление пароля";
        mailService.sendEmail(users, body, subject);

        response.setMessage("На Ваш почтовый ящик отправлена инструкция по восстановлению пароля.");
        return response;
    }

    @Secured("CHANGE_PASSWORD_PRIVILEGE")
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String showChangePasswordPage(Model model, @RequestParam("id") BigInteger id, @RequestParam("token") String token) {
        String result = passwordResetTokenService.validatePasswordResetToken(id, token);
        if (Objects.equals(result, "Valid")) {
            model.addAttribute("message", "SUCCESS");
            return "changepass";
        }
        return "redirect:/forgotPassword";
    }

    @PostMapping(value = "/savePassword")
    @ResponseBody
    public GenericResponse savePasswordPage(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changeUserPassword(user, searchSummary.getPassword());
        response.setMessage("Пароль успешно изменён.");
        return response;
    }

}
