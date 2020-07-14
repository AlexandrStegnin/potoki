package com.art.repository;

import com.art.model.Facility;
import com.art.model.supporting.UserFacilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Facility findById(Long id);

    Facility findByName(String facility);

    List<Facility> findAll();

    List<Facility> findByIdNot(Long id);

    List<Facility> findByIdIn(List<Long> idList);

    @Query(name = "InvestorsFacilities", nativeQuery = true)
    List<UserFacilities> getInvestorsFacility(Long rentorInvestorId);

}
