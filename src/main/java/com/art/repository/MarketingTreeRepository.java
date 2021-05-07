package com.art.repository;

import com.art.model.MarketingTree;
import com.art.model.supporting.dto.MarketingTreeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Transactional
public interface MarketingTreeRepository extends JpaRepository<MarketingTree, BigInteger>, JpaSpecificationExecutor<MarketingTree> {

    List<MarketingTree> findAll();

    Page<MarketingTree> findAll(Specification<MarketingTree> specification, Pageable pageable);

    @Query(value = "SELECT new com.art.model.supporting.dto.MarketingTreeDTO(" +
            "inv.partner.id, inv.id, inv.kin, MIN(ic.dateGiven), 'NO_ACTIVE', 0, 0) " +
            "FROM Money ic " +
            "JOIN ic.investor inv " +
            "WHERE inv.login <> 'investor-demo' AND inv.partner IS NOT NULL AND (ic.newCashDetail.id IS NULL OR ic.newCashDetail.id <> 4)" +
            "GROUP BY ic.investor " +
            "ORDER BY inv.partner.id, MIN(ic.dateGiven), inv.id")
    List<MarketingTreeDTO> findGroupedCash();

    void deleteByInvestorId(Long investorId);

    @Procedure(procedureName = "calculate_marketing_tree")
    void calculateMarketingTree();
}
