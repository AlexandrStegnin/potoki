package com.art.repository;

import com.art.model.Facility;
import com.art.model.supporting.UserFacilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, BigInteger> {

    Facility findById(BigInteger id);

    Facility findByName(String facility);

    List<Facility> findAll();

    List<Facility> findByIdNot(BigInteger id);

    List<Facility> findByIdIn(List<BigInteger> idList);

    @Query(name = "InvestorsFacilities", nativeQuery = true)
    List<UserFacilities> getInvestorsFacility(Long rentorInvestorId);

}
