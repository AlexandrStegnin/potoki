package com.art.converter;

import com.art.model.InvestorsTypes;
import com.art.service.InvestorsTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToInvestorsTypesConverter implements Converter<String, InvestorsTypes> {
    @Resource(name = "investorsTypesService")
    private InvestorsTypesService investorsTypesService;

    @Autowired
    public StringToInvestorsTypesConverter(InvestorsTypesService investorsTypesService) {
        this.investorsTypesService = investorsTypesService;
    }

    public InvestorsTypes convert(String id) {
        BigInteger IntId = new BigInteger(id);
        return investorsTypesService.findById(IntId);
    }
}
