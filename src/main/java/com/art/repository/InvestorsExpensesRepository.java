package com.art.repository;

import com.art.model.InvestorsExpenses;
import com.art.model.supporting.InvestorsPlanSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface InvestorsExpensesRepository extends JpaRepository<InvestorsExpenses, BigInteger>{

    List<InvestorsExpenses> findByInvestor_Id(BigInteger investorId);

    @Query(name = "InvestorsPlanSale", nativeQuery = true)
    List<InvestorsPlanSale> getInvestorsPlanSale(BigInteger investorId);

}
