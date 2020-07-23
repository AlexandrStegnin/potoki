package com.art.service;

import com.art.model.NewCashDetail;
import com.art.repository.NewCashDetailRepository;
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

    public NewCashDetail create(NewCashDetail newCashDetail) {
        return newCashDetailRepository.saveAndFlush(newCashDetail);
    }

    public List<NewCashDetail> findAll() {
        return newCashDetailRepository.findAll();
    }

    public NewCashDetail findById(Long id) {
        return newCashDetailRepository.findOne(id);
    }

    public NewCashDetail update(NewCashDetail newCashDetail) {
        return newCashDetailRepository.saveAndFlush(newCashDetail);
    }

    public NewCashDetail findByName(String name) {
        return newCashDetailRepository.findByName(name);
    }

    public void deleteById(Long id) {
        newCashDetailRepository.delete(id);
    }

    public List<NewCashDetail> initializeNewCashDetails() {
        List<NewCashDetail> newCashDetailList = new ArrayList<>(0);
        NewCashDetail newCashDetail = new NewCashDetail();
        newCashDetail.setId(0L);
        newCashDetail.setName("Выберите детали новых денег");
        newCashDetailList.add(newCashDetail);
        newCashDetailList.addAll(newCashDetailRepository.findAll());
        return newCashDetailList;
    }
}
