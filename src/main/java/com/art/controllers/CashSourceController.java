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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
        model.addAttribute("cashSourceDTO", new CashSourceDTO());
        return "cash-source-list";
    }

    @PostMapping(path = Location.CASH_SOURCES_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveCashSource(@RequestBody CashSourceDTO cashSourceDTO) {
        CashSource cashSource = new CashSource(cashSourceDTO);
        cashSourceService.create(cashSource);
        return new ApiResponse(getMessage(cashSource.getName(), "добавлен"), HttpStatus.OK.value());
    }

    @PostMapping(path = Location.CASH_SOURCES_UPDATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse updateSource(@RequestBody CashSourceDTO cashSourceDTO) {
        CashSource cashSource = new CashSource(cashSourceDTO);
        cashSourceService.update(cashSource);
        return new ApiResponse(getMessage(cashSource.getName(), "изменён"), HttpStatus.OK.value());
    }

    @PostMapping(path = Location.CASH_SOURCES_DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse deleteSource(@RequestBody CashSourceDTO dto) {
        cashSourceService.deleteById(dto.getId());
        return new ApiResponse(getMessage("", "удалён"), HttpStatus.OK.value());
    }

    @ResponseBody
    @PostMapping(path = Location.CASH_SOURCES_FIND)
    public CashSourceDTO findFacility(@RequestBody CashSourceDTO dto) {
        return new CashSourceDTO(cashSourceService.findById(dto.getId()));
    }

    /**
     * Сформировать сообщение
     *
     * @param sourceName название источника денег
     * @param action действие
     * @return сформированное сообщение
     */
    private String getMessage(String sourceName, String action) {
        String template = "Источник денег %s успешно %s";
        return String.format(template, sourceName, action);
    }

}
