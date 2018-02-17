package com.art.service;

import com.art.model.CashPayments;
import com.art.repository.CashPaymentsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class CashPaymentsService {

    @Resource(name = "cashPaymentsRepository")
    private CashPaymentsRepository cashPaymentsRepository;

    public CashPayments update(CashPayments cashPayments) {
        return cashPaymentsRepository.saveAndFlush(cashPayments);
    }

    public void deleteById(BigInteger id) {
        cashPaymentsRepository.delete(id);
    }

    public CashPayments create(CashPayments cashPayments) {
        return cashPaymentsRepository.saveAndFlush(cashPayments);
    }

    public List<CashPayments> findByManagerId(BigInteger managerId) {
        return cashPaymentsRepository.findByManagerId(managerId);
    }

    public CashPayments findById(BigInteger id) {
        return cashPaymentsRepository.findOne(id);
    }

}

