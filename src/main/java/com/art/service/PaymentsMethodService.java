package com.art.service;

import com.art.model.PaymentsMethod;
import com.art.repository.PaymentsMethodRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class PaymentsMethodService {

    @Resource(name = "paymentsMethodRepository")
    private PaymentsMethodRepository paymentsMethodRepository;

    public List<PaymentsMethod> findAll() {
        return paymentsMethodRepository.findAll();
    }

    public PaymentsMethod findById(BigInteger id){
        return paymentsMethodRepository.findOne(id);
    }

    public PaymentsMethod findByPayment(String payment){
        return paymentsMethodRepository.findByPayment(payment);
    }

    public List<PaymentsMethod> findByManager(int manager){
        return paymentsMethodRepository.findByManager(manager);
    }
}
