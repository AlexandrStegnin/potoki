package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Account;
import com.art.model.Room;
import com.art.model.UnderFacility;
import com.art.model.supporting.enums.OwnerType;
import com.art.service.AccountService;
import com.art.service.RoomService;
import com.art.service.UnderFacilityService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RoomController {

    private static final String REDIRECT_URL = Location.ROOMS_LIST;

    private final RoomService roomService;

    private final UnderFacilityService underFacilityService;

    private final AccountService accountService;

    public RoomController(RoomService roomService, UnderFacilityService underFacilityService,
                          AccountService accountService) {
        this.roomService = roomService;
        this.underFacilityService = underFacilityService;
        this.accountService = accountService;
    }

    @GetMapping(path = Location.ROOMS_LIST)
    public String roomsPage(ModelMap model) {
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "room-list";
    }

    @GetMapping(path = Location.ROOMS_EDIT)
    public String editRoom(@PathVariable Long id, ModelMap model) {
        String title = "Обновление данных по помещению";
        Room room = roomService.findByIdWithUnderFacilities(id);
        Account account = accountService.findByOwnerId(id, OwnerType.ROOM);
        String accountNumber = "";
        if (account != null) {
            accountNumber = account.getAccountNumber();
        }
        model.addAttribute("room", room);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        model.addAttribute("accountNumber", accountNumber);
        return "room-add";
    }

    @PostMapping(path = Location.ROOMS_EDIT)
    public String updateRoom(@ModelAttribute("rooms") Room room,
                             BindingResult result, ModelMap model) {
        String ret = "списку помещений.";
        if (result.hasErrors()) {
            return "room-add";
        }
        roomService.update(room);
        model.addAttribute("success", "Данные по помещению " + room.getName() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", REDIRECT_URL);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(path = Location.ROOMS_DELETE)
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
        return "redirect:" + REDIRECT_URL;
    }

    @GetMapping(path = Location.ROOMS_CREATE)
    public String newRoom(ModelMap model) {
        String title = "Добавление помещения";
        Room room = new Room();
        model.addAttribute("room", room);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "room-add";
    }

    @PostMapping(path = Location.ROOMS_CREATE)
    public String saveRoom(@ModelAttribute("room") Room room,
                           BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "room-add";
        }
        String ret = "списку помещений";
        roomService.create(room);
        model.addAttribute("success", "Помещение " + room.getName() +
                " успешно добавлено.");
        model.addAttribute("redirectUrl", REDIRECT_URL);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacility> initializeUnderFacilities() {
        return underFacilityService.initializeUnderFacilities();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
