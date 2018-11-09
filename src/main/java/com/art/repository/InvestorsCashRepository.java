package com.art.repository;

import com.art.model.InvestorsCash;
import com.art.model.supporting.InvestorsTotalSum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface InvestorsCashRepository extends JpaRepository<InvestorsCash, BigInteger>{
    InvestorsCash findById(BigInteger id);
    void deleteById(BigInteger id);

    @Query(name = "InvestorsTotalSum", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsTotalSum(BigInteger investorId);

    @Query(name = "InvestorsTotalSumDetails", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsTotalSumDetails(BigInteger investorId);

    @Query(name = "InvestorsCashSums", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsCashSums(BigInteger investorId);

    @Query(name = "InvestorsCashSumsDetails", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsCashSumsDetails(BigInteger investorId);

    List<InvestorsCash> findByInvestor_Id(BigInteger investorId);

    List<InvestorsCash> findAllByOrderByDateGivedCashAsc();

    List<InvestorsCash> findByRoomId(BigInteger roomId);

}
