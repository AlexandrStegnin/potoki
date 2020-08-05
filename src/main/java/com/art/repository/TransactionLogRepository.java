package com.art.repository;

import com.art.model.InvestorCash;
import com.art.model.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

    Page<TransactionLog> findAll(Specification<TransactionLog> filter, Pageable pageable);

    List<TransactionLog> findByInvestorCashesContains(InvestorCash cash);

    List<TransactionLog> findByBlockedFromId(Long blockedFromId);

}
