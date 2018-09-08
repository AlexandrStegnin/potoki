package com.art.service;

import com.art.model.CashSources;
import com.art.repository.CashSourcesRepository;
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
public class CashSourcesService {

    @Resource(name = "cashSourcesRepository")
    private CashSourcesRepository cashSourcesRepository;

    public List<CashSources> findAll() {
        return cashSourcesRepository.findAll();
    }

    public CashSources create(CashSources cashSources) {
        return cashSourcesRepository.saveAndFlush(cashSources);
    }

    public void delete(CashSources cashSources) {
        cashSourcesRepository.delete(cashSources);
    }

    public void deleteById(BigInteger id) {
        cashSourcesRepository.delete(id);
    }


    public CashSources update(CashSources cashSources) {
        return cashSourcesRepository.saveAndFlush(cashSources);
    }

    public CashSources findById(BigInteger id) {
        return cashSourcesRepository.findOne(id);
    }

    public List<CashSources> initializeCashSources() {
        List<CashSources> cashSources = new ArrayList<>(0);
        CashSources cashSource = new CashSources();
        cashSource.setId(new BigInteger("0"));
        cashSource.setCashSource("Выберите источник денег");
        cashSources.add(cashSource);
        cashSources.addAll(cashSourcesRepository.findAll());
        return cashSources;
    }

    public CashSources findByCashSource(String cashSource) {
        return cashSourcesRepository.findByCashSource(cashSource);
    }
}
