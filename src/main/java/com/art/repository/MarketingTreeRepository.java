package com.art.repository;

import com.art.model.MarketingTree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Transactional
public interface MarketingTreeRepository extends JpaRepository<MarketingTree, BigInteger>, JpaSpecificationExecutor<MarketingTree> {

    Page<MarketingTree> findAll(Specification<MarketingTree> specification, Pageable pageable);

    void deleteByInvestorId(Long investorId);

    @Procedure(procedureName = "calculate_marketing_tree")
    void calculateMarketingTree();
}
