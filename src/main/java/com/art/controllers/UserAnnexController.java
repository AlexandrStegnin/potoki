package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.model.Users;
import com.art.model.UsersAnnexToContracts;
import com.art.service.UsersAnnexToContractsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@RestController
public class UserAnnexController {

    private final UsersAnnexToContractsService annexService;

    public UserAnnexController(UsersAnnexToContractsService annexService) {
        this.annexService = annexService;
    }

    @PostMapping(path = "/have-unread", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean haveUnread(@RequestBody Users user) {
        String login = user.getLogin();
        if (null == login) {
            login = SecurityUtils.getUsername();
        }
        return annexService.haveUnread(login);
    }

    @PostMapping(value = "/get-annexes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UsersAnnexToContracts> getAnnexes(@RequestBody Users user) {
        String login = user.getLogin();
        if (null == login) {
            login = SecurityUtils.getUsername();
        }
        return annexService.findByLogin(login);
    }

    @PostMapping(value = "/mark-read-annex", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveAnnexRead(@RequestBody UsersAnnexToContracts annex) {
        UsersAnnexToContracts usersAnnexToContracts = annexService.findById(annex.getId());
        usersAnnexToContracts.setAnnexRead(1);
        usersAnnexToContracts.setDateRead(new Date());
        annexService.update(usersAnnexToContracts);
        return "success";
    }
}
