package com.art.repository;

import com.art.model.PaymentsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PaymentsTypeRepository extends JpaRepository<PaymentsType, BigInteger>{
    PaymentsType findByType(String type);
}
