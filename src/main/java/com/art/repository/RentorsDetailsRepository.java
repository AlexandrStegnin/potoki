package com.art.repository;

import com.art.model.RentorsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface RentorsDetailsRepository extends JpaRepository<RentorsDetails, BigInteger>{
    List<RentorsDetails> findByFacilityIdOrRentorId(BigInteger facilityId, BigInteger rentorId);
    List<RentorsDetails> findByRentorId(BigInteger rentorId);
    List<RentorsDetails> findByFacilityId(BigInteger facilityId);
}
