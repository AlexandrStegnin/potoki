package com.art.repository;

import com.art.model.MarketingTree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Transactional
public interface MarketingTreeRepository extends JpaRepository<MarketingTree, BigInteger>, JpaSpecificationExecutor<MarketingTree> {

    List<MarketingTree> findAll();

    Page<MarketingTree> findAll(Specification<MarketingTree> specification, Pageable pageable);
}
