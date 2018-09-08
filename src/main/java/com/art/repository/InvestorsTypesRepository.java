package com.art.repository;

import com.art.model.InvestorsTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface InvestorsTypesRepository extends JpaRepository<InvestorsTypes, BigInteger> {
    InvestorsTypes findByInvestorsType(String investorsType);
}
