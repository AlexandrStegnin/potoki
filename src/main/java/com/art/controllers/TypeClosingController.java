package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.TypeClosing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.TypeClosingDTO;
import com.art.service.TypeClosingService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TypeClosingController {

    private final TypeClosingService typeClosingService;

    public TypeClosingController(TypeClosingService typeClosingService) {
        this.typeClosingService = typeClosingService;
    }

    @GetMapping(path = Location.TYPE_CLOSING_LIST)
    public ModelAndView typeClosingInvestPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("type-closing-list");
        List<TypeClosing> typeClosingInvestsList = typeClosingService.findAll();
        modelAndView.addObject("typeClosingInvestsList", typeClosingInvestsList);
        modelAndView.addObject("typeClosingDTO", new TypeClosingDTO());
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(path = Location.TYPE_CLOSING_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse createType(@RequestBody TypeClosingDTO dto) {
        return typeClosingService.create(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.TYPE_CLOSING_UPDATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse updateType(@RequestBody TypeClosingDTO dto) {
        return typeClosingService.update(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.TYPE_CLOSING_DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse deleteType(@RequestBody TypeClosingDTO dto) {
        return typeClosingService.deleteById(dto.getId());
    }

    @ResponseBody
    @PostMapping(path = Location.TYPE_CLOSING_FIND)
    public TypeClosingDTO findType(@RequestBody TypeClosingDTO dto) {
        return new TypeClosingDTO(typeClosingService.findById(dto.getId()));
    }
}
