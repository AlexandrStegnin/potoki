package com.art.repository;

import com.art.model.NewCashDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface NewCashDetailsRepository extends JpaRepository<NewCashDetails, BigInteger> {

    NewCashDetails findByNewCashDetail(String newCashDetail);
}
