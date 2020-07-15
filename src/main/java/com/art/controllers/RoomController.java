package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Room;
import com.art.model.UnderFacility;
import com.art.service.RoomService;
import com.art.service.UnderFacilityService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RoomController {

    private final RoomService roomService;

    private final UnderFacilityService underFacilityService;

    public RoomController(RoomService roomService, UnderFacilityService underFacilityService) {
        this.roomService = roomService;
        this.underFacilityService = underFacilityService;
    }

    @GetMapping(path = Location.ROOMS_LIST)
    public String roomsPage(ModelMap model) {
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "room-list";
    }

    @GetMapping(path = Location.ROOMS_EDIT)
    public String editRoom(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по помещению";
        Room room = roomService.findByIdWithUnderFacilities(id);
        model.addAttribute("room", room);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "room-add";
    }

    @PostMapping(path = Location.ROOMS_EDIT)
    public String updateRoom(@ModelAttribute("rooms") Room room,
                             BindingResult result, ModelMap model) {
        String ret = "списку помещений.";
        String redirectUrl = "/rooms/list";
        if (result.hasErrors()) {
            return "room-add";
        }
        roomService.update(room);
        model.addAttribute("success", "Данные по помещению " + room.getName() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(path = Location.ROOMS_DELETE)
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
        return "redirect:/rooms/list";
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
        String redirectUrl = "/rooms/list";
        roomService.create(room);
        model.addAttribute("success", "Помещение " + room.getName() +
                " успешно добавлено.");
        model.addAttribute("redirectUrl", redirectUrl);
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
