package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Facility;
import com.art.model.Room;
import com.art.model.UnderFacility;
import com.art.service.FacilityService;
import com.art.service.RoomService;
import com.art.service.UnderFacilityService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Transactional
public class UnderFacilityController {

    private static final String REDIRECT_URL = Location.UNDER_FACILITIES_LIST;

    private final UnderFacilityService underFacilityService;

    private final FacilityService facilityService;

    private final RoomService roomService;

    public UnderFacilityController(UnderFacilityService underFacilityService, FacilityService facilityService,
                                   RoomService roomService) {
        this.underFacilityService = underFacilityService;
        this.facilityService = facilityService;
        this.roomService = roomService;
    }

    @GetMapping(path = Location.UNDER_FACILITIES_LIST)
    public String underFacilityPage(ModelMap model) {
        List<UnderFacility> underFacilities = underFacilityService.findAll();
        model.addAttribute("underFacilities", underFacilities);
        return "under-facility-list";
    }

    @GetMapping(path = Location.UNDER_FACILITIES_EDIT)
    public String editUnderFacility(@PathVariable Long id, ModelMap model) {
        String title = "Обновление данных по подобъекту";
        UnderFacility underFacility = underFacilityService.findById(id);
        model.addAttribute("underFacility", underFacility);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "under-facility-add";
    }

    @PostMapping(path = Location.UNDER_FACILITIES_EDIT)
    public String updateUnderFacility(@ModelAttribute("underFacility") UnderFacility underFacility,
                                      BindingResult result, ModelMap model) {
        String ret = "списку подобъектов.";
        if (result.hasErrors()) {
            return "under-facility-add";
        }
        underFacilityService.update(underFacility);
        model.addAttribute("success", "Данные по подобъекту " + underFacility.getName() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", REDIRECT_URL);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(path = Location.UNDER_FACILITIES_DELETE)
    public String deleteUnderFacility(@PathVariable Long id) {
        underFacilityService.deleteById(id);
        return "redirect:" + REDIRECT_URL;
    }

    @GetMapping(path = Location.UNDER_FACILITIES_CREATE)
    public String newUnderFacility(ModelMap model) {
        String title = "Добавление подобъекта";
        UnderFacility underFacility = new UnderFacility();
        model.addAttribute("underFacility", underFacility);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "under-facility-add";
    }

    @PostMapping(path = Location.UNDER_FACILITIES_CREATE)
    public String saveUnderFacility(@ModelAttribute("underFacility") UnderFacility underFacility,
                                    BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "under-facility-add";
        }
        String ret = "списку подобъектов";
        underFacilityService.create(underFacility);
        model.addAttribute("success", "Подобъект " + underFacility.getName() +
                " успешно добавлен.");
        model.addAttribute("redirectUrl", REDIRECT_URL);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("facilities")
    public List<Facility> initializeFacilities() {
        return facilityService.findAll();
    }

    @ModelAttribute("rooms")
    public List<Room> initializeR() {
        return roomService.init();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
