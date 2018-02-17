package com.art.repository;

import com.art.model.CashPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CashPaymentsRepository extends JpaRepository<CashPayments, BigInteger> {
    List<CashPayments> findByManagerId(BigInteger managerId);
}
