package com.art.service;

import com.art.config.application.Constant;
import com.art.model.CashSource;
import com.art.repository.CashSourceRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(Constant.CASH_SOURCES_CACHE_KEY)
    public List<CashSource> findAll() {
        return cashSourceRepository.findAll();
    }

    @CachePut(Constant.CASH_SOURCES_CACHE_KEY)
    public CashSource create(CashSource cashSource) {
        return cashSourceRepository.saveAndFlush(cashSource);
    }

    @CacheEvict(Constant.CASH_SOURCES_CACHE_KEY)
    public void deleteById(Long id) {
        cashSourceRepository.delete(id);
    }

    @CachePut(value = Constant.CASH_SOURCES_CACHE_KEY, key = "cashSource.id")
    public CashSource update(CashSource cashSource) {
        return cashSourceRepository.saveAndFlush(cashSource);
    }

    @Cacheable(Constant.CASH_SOURCES_CACHE_KEY)
    public CashSource findById(Long id) {
        return cashSourceRepository.findOne(id);
    }

    public List<CashSource> initializeCashSources() {
        List<CashSource> cashSources = new ArrayList<>(0);
        CashSource cashSource = new CashSource();
        cashSource.setId(0L);
        cashSource.setName("Выберите источник денег");
        cashSources.add(cashSource);
        cashSources.addAll(findAll());
        return cashSources;
    }
}
