package com.art.repository;

import com.art.model.CashSources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CashSourcesRepository extends JpaRepository<CashSources, BigInteger> {
    CashSources findByCashSource(String cashSource);
}
