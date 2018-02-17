package com.art.converter;

import com.art.model.CashSources;
import com.art.service.CashSourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToCashSourceConverter implements Converter<String, CashSources> {
    @Resource(name = "cashSourcesService")
    private CashSourcesService cashSourcesService;

    @Autowired
    public StringToCashSourceConverter(CashSourcesService cashSourcesService) {
        this.cashSourcesService = cashSourcesService;
    }

    public CashSources convert(String id) {
        BigInteger IntId = new BigInteger(id);
        CashSources cashSources;
        cashSources = cashSourcesService.findById(IntId);
        return cashSources;
    }
}
