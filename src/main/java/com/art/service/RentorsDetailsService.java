package com.art.service;

import com.art.model.RentorsDetails;
import com.art.repository.RentorsDetailsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class RentorsDetailsService {

    @Resource(name = "rentorsDetailsRepository")
    private RentorsDetailsRepository rentorsDetailsRepository;

    public List<RentorsDetails> findAll() {
        return rentorsDetailsRepository.findAll();
    }

    public RentorsDetails findById(BigInteger id) {
        return rentorsDetailsRepository.findOne(id);
    }

    public RentorsDetails create(RentorsDetails rentorsDetails) {
        return rentorsDetailsRepository.saveAndFlush(rentorsDetails);
    }

    public RentorsDetails update(RentorsDetails rentorsDetails) {
        return rentorsDetailsRepository.saveAndFlush(rentorsDetails);
    }

    public void deleteById(BigInteger id) {
        rentorsDetailsRepository.delete(id);
    }

    public List<RentorsDetails> findByFacilityIdOrRentorId(BigInteger facilityId, BigInteger rentorId) {
        return rentorsDetailsRepository.findByFacilityIdOrRentorId(facilityId, rentorId);
    }

    public List<RentorsDetails> findByRentorId(BigInteger rentorId) {
        return rentorsDetailsRepository.findByRentorId(rentorId);
    }

    public List<RentorsDetails> findByFacilityId(BigInteger facilityId) {
        return rentorsDetailsRepository.findByFacilityId(facilityId);
    }

    public List<RentorsDetails> createList(List<RentorsDetails> rdList) {
        return rentorsDetailsRepository.save(rdList);
    }

    public void deleteList(List<RentorsDetails> rentorsDetails) {
        rentorsDetailsRepository.delete(rentorsDetails);
    }
}
