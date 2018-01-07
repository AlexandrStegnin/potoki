package com.art.repository;

import com.art.model.CashTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CashTypesRepository extends JpaRepository<CashTypes, BigInteger> {
    CashTypes findByCashType(String cashType);
}
