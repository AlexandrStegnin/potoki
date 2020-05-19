package com.art.controllers;

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

    private final UsersAnnexToContractsService usersAnnexToContractsService;

    public UserAnnexController(UsersAnnexToContractsService annexService, UsersAnnexToContractsService usersAnnexToContractsService) {
        this.annexService = annexService;
        this.usersAnnexToContractsService = usersAnnexToContractsService;
    }

    @PostMapping(path = "/have-unread", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean haveUnread(@RequestBody Users user) {
        return annexService.haveUnread(user.getLogin());
    }

    @PostMapping(value = "/get-annexes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UsersAnnexToContracts> getAnnexes(@RequestBody Users user) {
        return usersAnnexToContractsService.findByLogin(user.getLogin());
    }

    @PostMapping(value = "/mark-read-annex", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveAnnexRead(@RequestBody UsersAnnexToContracts annex) {
        UsersAnnexToContracts usersAnnexToContracts = usersAnnexToContractsService.findById(annex.getId());
        usersAnnexToContracts.setAnnexRead(1);
        usersAnnexToContracts.setDateRead(new Date());
        usersAnnexToContractsService.update(usersAnnexToContracts);
        return "success";
    }
}
