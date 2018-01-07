package com.art.service;

import com.art.model.Bonuses;
import com.art.model.Users;
import com.art.repository.BonusesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class BonusesService {
    @Resource(name = "bonusesRepository")
    private BonusesRepository bonusesRepository;

    public Bonuses create(Bonuses bonuses){
        return bonusesRepository.saveAndFlush(bonuses);
    }

    public List<Bonuses> findAll(){
        return bonusesRepository.findAll();
    }

    public Bonuses findById(BigInteger id){
        return bonusesRepository.findOne(id);
    }

    public Bonuses update(Bonuses bonuses){
        return bonusesRepository.saveAndFlush(bonuses);
    }

    public void deleteById(BigInteger id){
        bonusesRepository.delete(id);
    }

    public List<Bonuses> findByManager(Users manager){
        return bonusesRepository.findByManager(manager);
    }

    public List<Bonuses> findByRentor(Users rentor){
        return bonusesRepository.findByRentor(rentor);
    }
    public List<Bonuses> updateList(List<Bonuses> bonuses){
        return bonusesRepository.save(bonuses);
    }
}
