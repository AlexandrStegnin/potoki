package com.art.repository;

import com.art.model.HistoryRelationships;
import com.art.model.supporting.InvestorsSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface HistoryRelationshipsRepository extends JpaRepository<HistoryRelationships, BigInteger> {
    HistoryRelationships findById(BigInteger id);
    List<HistoryRelationships> findByManagerIdOrRentorId(BigInteger managerId, BigInteger rentorId);


    @Query(name = "InvestorsSummary", nativeQuery = true)
    List<InvestorsSummary> getInvestorsSummary(BigInteger investorId);

    @Query(name = "InvestorsSummaryWithFacility", nativeQuery = true)
    List<InvestorsSummary> getInvestorsSummaryWithFacility(BigInteger investorId, String facility);

}
