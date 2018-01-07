package com.art.service;

import com.art.model.FacilitiesServiceContracts;
import com.art.model.Users;
import com.art.repository.FacilitiesServiceContractsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class FacilitiesServiceContractsService {

    @Resource(name = "facilitiesServiceContractsRepository")
    private FacilitiesServiceContractsRepository facilitiesServiceContractsRepository;

    public List<FacilitiesServiceContracts> findAll() {
        return facilitiesServiceContractsRepository.findAll();
    }

    public FacilitiesServiceContracts findById(BigInteger id){
        return facilitiesServiceContractsRepository.findById(id);
    }

    public FacilitiesServiceContracts update(FacilitiesServiceContracts facilitiesServiceContracts){
        return facilitiesServiceContractsRepository.saveAndFlush(facilitiesServiceContracts);
    }

    public void deleteById(BigInteger id){
        facilitiesServiceContractsRepository.delete(id);
    }

    public FacilitiesServiceContracts create(FacilitiesServiceContracts facilitiesServiceContracts){
        return facilitiesServiceContractsRepository.saveAndFlush(facilitiesServiceContracts);
    }

    public List<FacilitiesServiceContracts> findByRentor(Users user){
        return facilitiesServiceContractsRepository.findByRentor(user);
    }
}
