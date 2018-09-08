package com.art.repository;

import com.art.model.PaymentsMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PaymentsMethodRepository extends JpaRepository<PaymentsMethod, BigInteger>{
    PaymentsMethod findByPayment(String payment);
    List<PaymentsMethod> findByManager(int manager);
}
