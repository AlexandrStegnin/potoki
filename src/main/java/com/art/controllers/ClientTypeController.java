package com.art.controllers;

import com.art.model.ClientType;
import com.art.service.ClientTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.art.config.application.Location.*;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class ClientTypeController {

    private final ClientTypeService clientTypeService;

    public ClientTypeController(ClientTypeService clientTypeService) {
        this.clientTypeService = clientTypeService;
    }

    @GetMapping(path = CLIENT_TYPES)
    public ModelAndView getAll() {
        ModelAndView model = new ModelAndView("client-type-list");
        List<ClientType> clientTypes = clientTypeService.findAll();
        model.addObject("clientTypes", clientTypes);
        return model;
    }

    @ResponseBody
    @PostMapping(path = CLIENT_TYPES_CREATE)
    public ClientType create(@RequestBody ClientType clientType) {
        return clientTypeService.create(clientType);
    }

    @ResponseBody
    @PostMapping(path = CLIENT_TYPES_UPDATE)
    public ClientType update(@RequestBody ClientType clientType) {
        return clientTypeService.update(clientType);
    }

    @ResponseBody
    @PostMapping(path = CLIENT_TYPES_DELETE)
    public String delete(@RequestBody ClientType clientType) {
        clientTypeService.delete(clientType);
        return String.format("Вид клиента [%s] успешно удалён", clientType.getTitle());
    }

}
