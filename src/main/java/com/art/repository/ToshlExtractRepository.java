package com.art.repository;

import com.art.model.ToshlExtract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ToshlExtractRepository extends JpaRepository<ToshlExtract, BigInteger> {
}
