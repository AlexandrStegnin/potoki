package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Account;
import com.art.model.Room;
import com.art.model.UnderFacility;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.RoomDTO;
import com.art.model.supporting.enums.OwnerType;
import com.art.service.AccountService;
import com.art.service.RoomService;
import com.art.service.UnderFacilityService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@Transactional
public class RoomController {

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
        model.addAttribute("roomDTO", new RoomDTO());
        return "room-list";
    }

    @ResponseBody
    @PostMapping(path = Location.ROOMS_CREATE)
    public ApiResponse createRoom(@RequestBody RoomDTO dto) {
        return roomService.create(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.ROOMS_UPDATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse updateRoom(@RequestBody RoomDTO dto) {
        return roomService.update(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.ROOMS_DELETE)
    public ApiResponse deleteRoom(@RequestBody RoomDTO dto) {
        return roomService.deleteById(dto.getId());
    }

    @ResponseBody
    @PostMapping(path = Location.ROOMS_FIND)
    public RoomDTO findRoom(@RequestBody RoomDTO dto) {
        RoomDTO roomDTO = new RoomDTO(roomService.findById(dto.getId()));
        Account account = accountService.findByOwnerId(roomDTO.getId(), OwnerType.ROOM);
        if (Objects.nonNull(account)) {
            roomDTO.setAccountNumber(account.getAccountNumber());
        }
        return roomDTO;
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
