package com.art.converter;

import com.art.model.TypeClosingInvest;
import com.art.service.TypeClosingInvestService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;

@Component
public class StringToTypeClosingInvestConverter implements Converter<String, TypeClosingInvest> {

    @Resource(name = "typeClosingInvestService")
    private TypeClosingInvestService typeClosingInvestService;

    public StringToTypeClosingInvestConverter(TypeClosingInvestService typeClosingInvestService) {
        this.typeClosingInvestService = typeClosingInvestService;
    }

    public TypeClosingInvest convert(String id) {
        TypeClosingInvest typeClosingInvest;
        try {
            BigInteger IntId = new BigInteger(id);
            typeClosingInvest = typeClosingInvestService.findById(IntId);
        } catch (Exception ex) {
            typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest(id);
        }
        return typeClosingInvest;
    }

}
