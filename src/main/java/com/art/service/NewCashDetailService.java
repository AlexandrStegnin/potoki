package com.art.service;

import com.art.config.application.Constant;
import com.art.model.NewCashDetail;
import com.art.repository.NewCashDetailRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NewCashDetailService {

    private final NewCashDetailRepository newCashDetailRepository;

    public NewCashDetailService(NewCashDetailRepository newCashDetailRepository) {
        this.newCashDetailRepository = newCashDetailRepository;
    }

    @CachePut(Constant.NEW_CASH_DETAILS_CACHE_KEY)
    public NewCashDetail create(NewCashDetail newCashDetail) {
        return newCashDetailRepository.saveAndFlush(newCashDetail);
    }

    @Cacheable(Constant.NEW_CASH_DETAILS_CACHE_KEY)
    public List<NewCashDetail> findAll() {
        return newCashDetailRepository.findAll();
    }

    @Cacheable(Constant.NEW_CASH_DETAILS_CACHE_KEY)
    public NewCashDetail findById(Long id) {
        return newCashDetailRepository.findOne(id);
    }

    @CachePut(value = Constant.NEW_CASH_DETAILS_CACHE_KEY, key = "newCashDetail.id")
    public NewCashDetail update(NewCashDetail newCashDetail) {
        return newCashDetailRepository.saveAndFlush(newCashDetail);
    }

    @Cacheable(Constant.NEW_CASH_DETAILS_CACHE_KEY)
    public NewCashDetail findByName(String name) {
        return newCashDetailRepository.findByName(name);
    }

    @CacheEvict(Constant.NEW_CASH_DETAILS_CACHE_KEY)
    public void deleteById(Long id) {
        newCashDetailRepository.delete(id);
    }

    public List<NewCashDetail> initializeNewCashDetails() {
        List<NewCashDetail> newCashDetailList = new ArrayList<>(0);
        NewCashDetail newCashDetail = new NewCashDetail();
        newCashDetail.setId(0L);
        newCashDetail.setName("Выберите детали новых денег");
        newCashDetailList.add(newCashDetail);
        newCashDetailList.addAll(findAll());
        return newCashDetailList;
    }
}
