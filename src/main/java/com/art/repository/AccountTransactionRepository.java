package com.art.repository;

import com.art.model.AccountTransaction;
import com.art.model.supporting.enums.CashType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long>, JpaSpecificationExecutor<AccountTransaction> {

    Page<AccountTransaction> findAll(Specification<AccountTransaction> specification, Pageable pageable);

    List<AccountTransaction> findAll(Specification<AccountTransaction> specification);

    @Query("SELECT DISTINCT atx.owner.ownerName FROM AccountTransaction atx ORDER BY atx.owner.ownerName")
    List<String> getAllOwners();

    @Query("SELECT DISTINCT atx.payer.ownerName FROM AccountTransaction atx ORDER BY atx.payer.ownerName")
    List<String> getAllRecipients();

    @Query("SELECT DISTINCT atx.cashType FROM AccountTransaction atx")
    List<CashType> getAllCashTypes();

}
