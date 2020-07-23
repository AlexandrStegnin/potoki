package com.art.converter;

import com.art.model.TypeClosing;
import com.art.service.TypeClosingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTypeClosingConverter implements Converter<String, TypeClosing> {

    private final TypeClosingService typeClosingService;

    @Autowired
    public StringToTypeClosingConverter(TypeClosingService typeClosingService) {
        this.typeClosingService = typeClosingService;
    }

    public TypeClosing convert(String id) {
        TypeClosing typeClosing;
        try {
            Long longId = Long.valueOf(id);
            typeClosing = typeClosingService.findById(longId);
        } catch (Exception ex) {
            typeClosing = typeClosingService.findByName(id);
        }
        return typeClosing;
    }

}
