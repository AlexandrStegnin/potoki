package com.art.converter;

import com.art.model.NewCashDetails;
import com.art.service.NewCashDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToNewCashDetailConverter implements Converter<String, NewCashDetails> {
    @Resource(name = "newCashDetailsService")
    private NewCashDetailsService newCashDetailsService;

    @Autowired
    public StringToNewCashDetailConverter(NewCashDetailsService newCashDetailsService) {
        this.newCashDetailsService = newCashDetailsService;
    }

    public NewCashDetails convert(String id) {
        BigInteger IntId = new BigInteger(id);
        NewCashDetails newCashDetails;
        newCashDetails = newCashDetailsService.findById(IntId);
        return newCashDetails;
    }
}
