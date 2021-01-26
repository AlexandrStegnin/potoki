package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Facility;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.FacilityDTO;
import com.art.service.FacilityService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Transactional
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping(path = Location.FACILITIES_CREATE)
    @ResponseBody
    public ApiResponse createFacility(@RequestBody FacilityDTO facilityDTO) {
        return facilityService.create(facilityDTO);
    }

    @GetMapping(path = Location.FACILITIES_LIST)
    public String getFacilities(ModelMap model) {
        List<Facility> facilities = facilityService.findAll();
        model.addAttribute("facilities", facilities);
        model.addAttribute("facilityDTO", new FacilityDTO());
        return "facility-list";
    }

    /**
     * Удаление объекта по ID.
     */
    @PostMapping(path = Location.FACILITIES_DELETE, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ApiResponse deleteFacility(@RequestBody FacilityDTO facilityDTO) {
        return facilityService.delete(facilityDTO);
    }

    @ResponseBody
    @PostMapping(path = Location.FACILITY_FIND)
    public FacilityDTO findFacility(@RequestBody FacilityDTO dto) {
        return new FacilityDTO(facilityService.findById(dto.getId()));
    }

    @ResponseBody
    @PostMapping(path = Location.FACILITIES_UPDATE)
    public ApiResponse updateFacility(@RequestBody FacilityDTO dto) {
        return facilityService.update(dto);
    }

}
