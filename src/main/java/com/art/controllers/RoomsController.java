package com.art.controllers;

import com.art.model.Rooms;
import com.art.model.UnderFacilities;
import com.art.service.RoomsService;
import com.art.service.UnderFacilitiesService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RoomsController {

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @GetMapping(value = "/rooms")
    public String roomsPage(ModelMap model) {

        List<Rooms> rooms = roomsService.findAll();
        model.addAttribute("rooms", rooms);

        return "viewRooms";
    }

    @GetMapping(value = {"/edit-room-{id}"})
    public String editRoom(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по помещению";
        Rooms room = roomsService.findByIdWithUnderFacilities(id);
        model.addAttribute("rooms", room);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addRooms";
    }

    @PostMapping(value = {"/edit-room-{id}"})
    public String updateRoom(@ModelAttribute("rooms") Rooms room,
                             BindingResult result, ModelMap model) {
        String ret = "списку помещений.";
        String redirectUrl = "/rooms";
        if (result.hasErrors()) {
            return "addRooms";
        }

        roomsService.update(room);

        model.addAttribute("success", "Данные по помещению " + room.getRoom() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-room-{id}"})
    public String deleteRoom(@PathVariable BigInteger id) {
        roomsService.deleteById(id);
        return "redirect:/rooms";
    }

    @GetMapping(value = {"/newRoom"})
    public String newRoom(ModelMap model) {
        String title = "Добавление помещения";
        Rooms room = new Rooms();
        model.addAttribute("rooms", room);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addRooms";
    }

    @PostMapping(value = {"/newRoom"})
    public String saveRoom(@ModelAttribute("rooms") Rooms room,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addRooms";
        }
        String ret = "списку помещений";
        String redirectUrl = "/rooms";

        roomsService.create(room);

        model.addAttribute("success", "Помещение " + room.getRoom() +
                " успешно добавлено.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacilities> initializeUnderFacilities() {
        return underFacilitiesService.initializeUnderFacilities();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
