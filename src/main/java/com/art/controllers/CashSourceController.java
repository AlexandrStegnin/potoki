package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.CashSource;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.CashSourceDTO;
import com.art.service.CashSourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CashSourceController {

    private final CashSourceService cashSourceService;

    public CashSourceController(CashSourceService cashSourceService) {
        this.cashSourceService = cashSourceService;
    }

    @GetMapping(path = Location.CASH_SOURCES_LIST)
    public String cashSourcesPage(ModelMap model) {
        List<CashSource> cashSources = cashSourceService.findAll();
        model.addAttribute("cashSources", cashSources);
        return "cash-source-list";
    }

    @GetMapping(path = Location.CASH_SOURCES_CREATE)
    public String newCashSource(ModelMap model) {
        String title = "Добавление источника денег";
        CashSource cashSource = new CashSource();
        model.addAttribute("cashSource", cashSource);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "cash-source-add";
    }

    @PostMapping(path = Location.CASH_SOURCES_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveCashSource(@RequestBody CashSourceDTO cashSourceDTO) {
        CashSource cashSource = new CashSource(cashSourceDTO);
        cashSourceService.create(cashSource);
        return new ApiResponse("Источник денег " + cashSource.getName() + " успешно добавлен.", HttpStatus.OK.value());
    }


    @GetMapping(path = Location.CASH_SOURCES_EDIT)
    public String editCashSource(@PathVariable Long id, ModelMap model) {
        String title = "Обновление источника получения денег";
        CashSource cashSource = cashSourceService.findById(id);

        model.addAttribute("cashSource", cashSource);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "cash-source-add";
    }

    @PostMapping(path = Location.CASH_SOURCES_EDIT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse updateSource(@RequestBody CashSourceDTO cashSourceDTO) {
        CashSource cashSource = new CashSource(cashSourceDTO);
        cashSourceService.update(cashSource);
        return new ApiResponse("Источник денег " + cashSource.getName() + " успешно изменён.", HttpStatus.OK.value());
    }

    @PostMapping(path = Location.CASH_SOURCES_DELETE, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ApiResponse deleteSource(@RequestBody CashSourceDTO dto) {
        cashSourceService.deleteById(dto.getId());
        return new ApiResponse("Источник успешно удалён.", HttpStatus.OK.value());
    }
}
