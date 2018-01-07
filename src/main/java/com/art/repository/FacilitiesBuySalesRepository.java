package com.art.repository;

import com.art.model.FacilitiesBuySales;
import com.art.model.UnderFacilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FacilitiesBuySalesRepository extends JpaRepository<FacilitiesBuySales, BigInteger>{
    void deleteByUnderFacility(UnderFacilities underFacility);
}
