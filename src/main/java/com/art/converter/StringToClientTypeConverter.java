package com.art.converter;

import com.art.model.ClientType;
import com.art.service.ClientTypeService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Alexandr Stegnin
 */

@Component
public class StringToClientTypeConverter implements Converter<String, ClientType> {

    @Resource(name = "clientTypeService")
    private final ClientTypeService clientTypeService;

    public StringToClientTypeConverter(ClientTypeService clientTypeService) {
        this.clientTypeService = clientTypeService;
    }

    public ClientType convert(String id) {
        ClientType clientType;
        try {
            Long intId = Long.valueOf(id);
            clientType = clientTypeService.findById(intId);
        } catch (Exception ex) {
            clientType = clientTypeService.findByTitle(id);
        }
        return clientType;
    }

}
