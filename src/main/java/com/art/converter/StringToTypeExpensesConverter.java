package com.art.converter;

import com.art.model.TypeExpenses;
import com.art.service.TypeExpensesService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;

@Component
public class StringToTypeExpensesConverter implements Converter<String, TypeExpenses> {

    @Resource(name = "typeExpensesService")
    private TypeExpensesService typeExpensesService;

    public StringToTypeExpensesConverter(TypeExpensesService typeExpensesService) {
        this.typeExpensesService = typeExpensesService;
    }

    public TypeExpenses convert(String id) {
        TypeExpenses typeExpenses;
        try {
            BigInteger IntId = new BigInteger(id);
            typeExpenses = typeExpensesService.findById(IntId);
        } catch (Exception ex) {
            typeExpenses = typeExpensesService.findByTypeExp(id);
        }
        return typeExpenses;
    }
}
