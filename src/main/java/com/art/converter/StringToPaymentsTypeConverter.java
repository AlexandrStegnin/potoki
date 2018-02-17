package com.art.converter;

import com.art.model.PaymentsType;
import com.art.service.PaymentsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.math.BigInteger;

public class StringToPaymentsTypeConverter implements Converter<String, PaymentsType> {

    @Autowired
    PaymentsTypeService paymentsTypeService;

    @Autowired
    public StringToPaymentsTypeConverter(PaymentsTypeService paymentsTypeService) {
        this.paymentsTypeService = paymentsTypeService;
    }

    public PaymentsType convert(String id) {
        PaymentsType paymentsType;
        try {
            BigInteger IntId = new BigInteger(id);
            paymentsType = paymentsTypeService.findById(IntId);
        } catch (Exception ex) {
            paymentsType = paymentsTypeService.findByType(id);
        }

        System.out.println("Payment type : " + paymentsType);
        return paymentsType;
    }
}
