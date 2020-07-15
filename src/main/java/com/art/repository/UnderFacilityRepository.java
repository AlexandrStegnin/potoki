package com.art.repository;

import com.art.model.UnderFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnderFacilityRepository extends JpaRepository<UnderFacility, Long> {

    UnderFacility findByName(String name);

    List<UnderFacility> findByFacilityId(Long facilityId);

}
