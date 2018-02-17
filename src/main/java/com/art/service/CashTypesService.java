package com.art.service;

import com.art.model.CashTypes;
import com.art.repository.CashTypesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Repository
public class CashTypesService {
    @Resource(name = "cashTypesRepository")
    private CashTypesRepository cashTypesRepository;

    public List<CashTypes> findAll() {
        return cashTypesRepository.findAll();
    }

    public CashTypes create(CashTypes cashTypes) {
        return cashTypesRepository.saveAndFlush(cashTypes);
    }

    public void delete(CashTypes cashTypes) {
        cashTypesRepository.delete(cashTypes);
    }

    public void deleteById(BigInteger id) {
        cashTypesRepository.delete(id);
    }


    public CashTypes update(CashTypes cashTypes) {
        return cashTypesRepository.saveAndFlush(cashTypes);
    }

    public CashTypes findById(BigInteger id) {
        return cashTypesRepository.findOne(id);
    }

    public CashTypes findByCashType(String cashType) {
        return cashTypesRepository.findByCashType(cashType);
    }

    public List<CashTypes> initializeCashTypes() {
        List<CashTypes> cashTypesList = new ArrayList<>(0);
        CashTypes cashTypes = new CashTypes();
        cashTypes.setId(new BigInteger("0"));
        cashTypes.setCashType("Выберите вид денег");
        cashTypesList.add(cashTypes);
        cashTypesList.addAll(cashTypesRepository.findAll());
        return cashTypesList;
    }
}
