package com.art.repository;

import com.art.model.InvestorsFlowsSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigInteger;

public interface InvestorsFlowsSaleRepository extends JpaRepository<InvestorsFlowsSale, BigInteger>, JpaSpecificationExecutor<InvestorsFlowsSale> {

    Page<InvestorsFlowsSale> findAll(Specification<InvestorsFlowsSale> specification, Pageable pageable);

}
