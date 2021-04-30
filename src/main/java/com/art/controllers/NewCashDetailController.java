package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.NewCashDetail;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.NewCashDetailDTO;
import com.art.service.NewCashDetailService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NewCashDetailController {

    private final NewCashDetailService newCashDetailService;

    public NewCashDetailController(NewCashDetailService newCashDetailService) {
        this.newCashDetailService = newCashDetailService;
    }

    @GetMapping(path = Location.NEW_CASH_DETAILS_LIST)
    public String newCashDetailsPage(ModelMap model) {
        List<NewCashDetail> newCashDetails = newCashDetailService.findAll();
        model.addAttribute("newCashDetails", newCashDetails);
        model.addAttribute("newCashDetailDTO", new NewCashDetailDTO());
        return "new-cash-detail-list";
    }

    @ResponseBody
    @PostMapping(path = Location.NEW_CASH_DETAILS_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse createDetail(@RequestBody NewCashDetailDTO dto) {
        return newCashDetailService.create(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.NEW_CASH_DETAILS_UPDATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse updateDetail(@RequestBody NewCashDetailDTO dto) {
        return newCashDetailService.update(dto);
    }

    @PostMapping(path = Location.NEW_CASH_DETAILS_DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse deleteSource(@RequestBody NewCashDetailDTO newCashDetailDTO) {
        newCashDetailService.deleteById(newCashDetailDTO.getId());
        return new ApiResponse("Детали новых денег успешно удалены.");
    }

    @ResponseBody
    @PostMapping(path = Location.NEW_CASH_DETAILS_FIND)
    public NewCashDetailDTO findDetail(@RequestBody NewCashDetailDTO dto) {
        return new NewCashDetailDTO(newCashDetailService.findById(dto.getId()));
    }

}
