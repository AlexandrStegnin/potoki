package com.art.service;

import com.art.model.NewCashDetails;
import com.art.repository.NewCashDetailsRepository;
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
public class NewCashDetailsService {
    @Resource(name = "newCashDetailsRepository")
    private NewCashDetailsRepository newCashDetailsRepository;

    public NewCashDetails create(NewCashDetails newCashDetails) {
        return newCashDetailsRepository.saveAndFlush(newCashDetails);
    }

    public List<NewCashDetails> findAll() {
        return newCashDetailsRepository.findAll();
    }

    public NewCashDetails findById(BigInteger id) {
        return newCashDetailsRepository.findOne(id);
    }

    public NewCashDetails update(NewCashDetails newCashDetails) {
        return newCashDetailsRepository.saveAndFlush(newCashDetails);
    }

    public NewCashDetails findByNewCashDetail(String newCashDetail) {
        return newCashDetailsRepository.findByNewCashDetail(newCashDetail);
    }

    public void deleteById(BigInteger id) {
        newCashDetailsRepository.delete(id);
    }

    public List<NewCashDetails> initializeNewCashDetails() {
        List<NewCashDetails> newCashDetailsList = new ArrayList<>(0);
        NewCashDetails newCashDetails = new NewCashDetails();
        newCashDetails.setId(new BigInteger("0"));
        newCashDetails.setNewCashDetail("Выберите детали новых денег");
        newCashDetailsList.add(newCashDetails);
        newCashDetailsList.addAll(newCashDetailsRepository.findAll());
        return newCashDetailsList;
    }
}
