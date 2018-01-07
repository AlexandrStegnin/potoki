package com.art.repository;

import com.art.model.UnderFacilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface UnderFacilitiesRepository extends JpaRepository<UnderFacilities, BigInteger>{
    UnderFacilities findByUnderFacility(String underFacility);
    List<UnderFacilities> findByFacilityId(BigInteger facilityId);
}
