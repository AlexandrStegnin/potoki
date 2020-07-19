package com.art.service;

import com.art.model.CashSource;
import com.art.repository.CashSourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CashSourceService {

    private final CashSourceRepository cashSourceRepository;

    public CashSourceService(CashSourceRepository cashSourceRepository) {
        this.cashSourceRepository = cashSourceRepository;
    }

    public List<CashSource> findAll() {
        return cashSourceRepository.findAll();
    }

    public CashSource create(CashSource cashSource) {
        return cashSourceRepository.saveAndFlush(cashSource);
    }

    public void delete(CashSource cashSource) {
        cashSourceRepository.delete(cashSource);
    }

    public void deleteById(Long id) {
        cashSourceRepository.delete(id);
    }

    public CashSource update(CashSource cashSource) {
        return cashSourceRepository.saveAndFlush(cashSource);
    }

    public CashSource findById(Long id) {
        return cashSourceRepository.findOne(id);
    }

    public List<CashSource> initializeCashSources() {
        List<CashSource> cashSources = new ArrayList<>(0);
        CashSource cashSource = new CashSource();
        cashSource.setId(0L);
        cashSource.setName("Выберите источник денег");
        cashSources.add(cashSource);
        cashSources.addAll(cashSourceRepository.findAll());
        return cashSources;
    }
}
