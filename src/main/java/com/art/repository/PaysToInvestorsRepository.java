package com.art.repository;

import com.art.model.supporting.PaysToInvestors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PaysToInvestorsRepository extends JpaRepository<PaysToInvestors, BigInteger> {
    List<PaysToInvestors> findAll();
    List<PaysToInvestors> findByInvestorIdAndFacility(
            BigInteger investorId, String facility);
}
