package com.art.service;


import com.art.model.PaymentsType;
import com.art.repository.PaymentsTypeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class PaymentsTypeService {

    @Resource(name = "paymentsTypeRepository")
    private PaymentsTypeRepository paymentsTypeRepository;

    public List<PaymentsType> findAll() {
        return paymentsTypeRepository.findAll();
    }

    public PaymentsType findById(BigInteger id) {
        return paymentsTypeRepository.findOne(id);
    }

    public PaymentsType findByType(String type) {
        return paymentsTypeRepository.findByType(type);
    }
}
