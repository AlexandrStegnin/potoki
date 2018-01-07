package com.art.service;

import com.art.model.AllowanceIp;
import com.art.model.Users;
import com.art.repository.AllowanceIpRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class AllowanceIpService {

    @Resource(name = "allowanceIpRepository")
    private AllowanceIpRepository allowanceIpRepository;

    public List<AllowanceIp> createList(List<AllowanceIp> allowanceIpList){
        return allowanceIpRepository.save(allowanceIpList);
    }

    public AllowanceIp create(AllowanceIp allowanceIp){
        return allowanceIpRepository.saveAndFlush(allowanceIp);
    }

    public List<AllowanceIp> findAll(){
        return allowanceIpRepository.findAll();
    }

    public AllowanceIp findById(BigInteger id){
        return allowanceIpRepository.findOne(id);
    }

    public AllowanceIp update(AllowanceIp allowanceIp){
        return allowanceIpRepository.saveAndFlush(allowanceIp);
    }

    public void deleteById(BigInteger id){
        allowanceIpRepository.delete(id);
    }

    public List<AllowanceIp> findByFacilityId(BigInteger facilityId){
        return allowanceIpRepository.findByFacilityId(facilityId);
    }

    public void updateList(List<AllowanceIp> allowanceIpList){
        allowanceIpRepository.save(allowanceIpList);
    }

    public List<AllowanceIp> findByInvestor(Users user){
        return allowanceIpRepository.findByInvestor(user);
    }

}
