package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.TypeClosing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.TypeClosingDTO;
import com.art.service.TypeClosingService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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
        return modelAndView;
    }

    @GetMapping(path = Location.TYPE_CLOSING_CREATE)
    public String newTypeClosing(ModelMap model) {
        String title = "Добавление вида закрытия";
        TypeClosing typeClosing = new TypeClosing();
        model.addAttribute("typeClosing", typeClosing);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "type-closing-add";
    }

    @PostMapping(path = Location.TYPE_CLOSING_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveTypeClosing(@RequestBody TypeClosingDTO typeClosingDTO) {
        TypeClosing typeClosing = new TypeClosing(typeClosingDTO);
        typeClosingService.create(typeClosing);
        return new ApiResponse("Вид закрытия [" + typeClosing.getName() + "] успешно добавлен.");
    }


    @GetMapping(path = Location.TYPE_CLOSING_EDIT)
    public String editTypeClosing(@PathVariable Long id, ModelMap model) {
        String title = "Обновление вида закрытия";
        TypeClosing typeClosing = typeClosingService.findById(id);
        model.addAttribute("typeClosing", typeClosing);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "type-closing-add";
    }

    @PostMapping(path = Location.TYPE_CLOSING_EDIT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse updateTypeClosing(@RequestBody TypeClosingDTO typeClosingDTO) {
        TypeClosing typeClosing = typeClosingService.findById(typeClosingDTO.getId());
        typeClosing.setName(typeClosingDTO.getName());
        typeClosingService.update(typeClosing);
        return new ApiResponse("Вид закрытия [" + typeClosing.getName() + "] успешно изменён.");
    }

    @PostMapping(path = Location.TYPE_CLOSING_DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse deleteTypeClosing(@RequestBody TypeClosingDTO typeClosingDTO) {
        typeClosingService.deleteById(typeClosingDTO.getId());
        return new ApiResponse("Вид закрытия успешно удалён.");
    }
}
