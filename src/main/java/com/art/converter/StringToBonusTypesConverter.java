package com.art.converter;

import com.art.model.BonusTypes;
import com.art.service.BonusTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToBonusTypesConverter implements Converter<String, BonusTypes> {

    @Resource(name = "bonusTypesService")
    private BonusTypesService bonusTypesService;

    @Autowired
    public StringToBonusTypesConverter(BonusTypesService bonusTypesService) {
        this.bonusTypesService = bonusTypesService;
    }

    public BonusTypes convert(String id) {
        BonusTypes bonusTypes;
        try {
            BigInteger IntId = new BigInteger(id);
            bonusTypes = bonusTypesService.findById(IntId);
        } catch (Exception ex) {
            bonusTypes = bonusTypesService.findByBonusType(id);
        }

        return bonusTypes;
    }
}
