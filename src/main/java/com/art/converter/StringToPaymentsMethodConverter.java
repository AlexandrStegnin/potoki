package com.art.converter;

import com.art.model.PaymentsMethod;
import com.art.service.PaymentsMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToPaymentsMethodConverter implements Converter<String, PaymentsMethod> {

    @Resource(name = "paymentsMethodService")
    private PaymentsMethodService paymentsMethodService;

    @Autowired
    public StringToPaymentsMethodConverter(PaymentsMethodService paymentsMethodService){
        this.paymentsMethodService = paymentsMethodService;
    }

    public PaymentsMethod convert(String id) {
        PaymentsMethod paymentsMethod = null;
        try{
            BigInteger IntId = new BigInteger(id);
            paymentsMethod = paymentsMethodService.findById(IntId);
        }catch(Exception ex){
            paymentsMethod = paymentsMethodService.findByPayment(id);
        }

        System.out.println("Payment method : " + paymentsMethod);
        return paymentsMethod;
    }
}
