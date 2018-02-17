package com.art.service;

import com.art.model.BonusTypes;
import com.art.repository.BonusTypesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class BonusTypesService {

    @Resource(name = "bonusTypesRepository")
    private BonusTypesRepository bonusTypesRepository;

    public BonusTypes create(BonusTypes bonusTypes) {
        return bonusTypesRepository.saveAndFlush(bonusTypes);
    }

    public List<BonusTypes> findAll() {
        return bonusTypesRepository.findAll();
    }

    public BonusTypes findById(BigInteger id) {
        return bonusTypesRepository.findOne(id);
    }

    public BonusTypes update(BonusTypes bonusTypes) {
        return bonusTypesRepository.saveAndFlush(bonusTypes);
    }

    public void deleteById(BigInteger id) {
        bonusTypesRepository.delete(id);
    }

    public BonusTypes findByBonusType(String bonusType) {
        return bonusTypesRepository.findByBonusType(bonusType);
    }
}
