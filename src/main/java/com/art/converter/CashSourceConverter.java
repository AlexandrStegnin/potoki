package com.art.converter;

import com.art.model.CashSource;
import com.art.service.CashSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class CashSourceConverter implements Converter<String, CashSource> {

    @Autowired
    private final CashSourceService cashSourceService;

    @Autowired
    public CashSourceConverter(CashSourceService cashSourceService) {
        this.cashSourceService = cashSourceService;
    }

    public CashSource convert(String id) {
        Long IntId = Long.valueOf(id);
        CashSource cashSource;
        cashSource = cashSourceService.findById(IntId);
        return cashSource;
    }
}
