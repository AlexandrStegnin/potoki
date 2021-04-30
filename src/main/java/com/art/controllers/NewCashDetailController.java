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

    @GetMapping(path = Location.NEW_CASH_DETAILS_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String newCashDetail(ModelMap model) {
        String title = "Добавление деталей новых денег";
        NewCashDetail newCashDetail = new NewCashDetail();
        model.addAttribute("newCashDetail", newCashDetail);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "new-cash-detail-add";
    }

    @PostMapping(path = Location.NEW_CASH_DETAILS_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveNewCashDetail(@RequestBody NewCashDetailDTO newCashDetailDTO) {
        NewCashDetail newCashDetail = new NewCashDetail(newCashDetailDTO);
        newCashDetailService.create(newCashDetail);
        return new ApiResponse("Детали новых денег [" + newCashDetail.getName() + "] успешно добавлены.");
    }


    @GetMapping(path = Location.NEW_CASH_DETAILS_EDIT)
    public String editNewCashDetail(@PathVariable Long id, ModelMap model) {
        String title = "Обновление деталей новых денег";
        NewCashDetail newCashDetail = newCashDetailService.findById(id);
        model.addAttribute("newCashDetail", newCashDetail);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "new-cash-detail-add";
    }

    @PostMapping(path = Location.NEW_CASH_DETAILS_EDIT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse updateNewCashDetail(@RequestBody NewCashDetailDTO newCashDetailDTO) {
        NewCashDetail newCashDetail = newCashDetailService.findById(newCashDetailDTO.getId());
        newCashDetail.setName(newCashDetailDTO.getName());
        newCashDetailService.update(newCashDetail);
        return new ApiResponse("Детали новых денег [" + newCashDetail.getName() + "] успешно изменены.");
    }

    @PostMapping(path = Location.NEW_CASH_DETAILS_DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse deleteSource(@RequestBody NewCashDetailDTO newCashDetailDTO) {
        newCashDetailService.deleteById(newCashDetailDTO.getId());
        return new ApiResponse("Детали новых денег успешно удалены.");
    }
}
