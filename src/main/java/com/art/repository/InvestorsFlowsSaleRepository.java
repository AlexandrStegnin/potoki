package com.art.repository;

import com.art.model.SalePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface InvestorsFlowsSaleRepository extends JpaRepository<SalePayment, BigInteger>, JpaSpecificationExecutor<SalePayment> {

    Page<SalePayment> findAll(Specification<SalePayment> specification, Pageable pageable);

}
