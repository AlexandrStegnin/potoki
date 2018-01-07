package com.art.converter;

import com.art.model.CashTypes;
import com.art.service.CashTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToCashTypeConverter implements Converter<String, CashTypes> {
    @Resource(name = "cashTypesService")
    private CashTypesService cashTypesService;

    @Autowired
    public StringToCashTypeConverter(CashTypesService cashTypesService){
        this.cashTypesService = cashTypesService;
    }

    public CashTypes convert(String id) {
        BigInteger IntId = new BigInteger(id);
        CashTypes cashTypes;
        cashTypes = cashTypesService.findById(IntId);
        return cashTypes;
    }
}
