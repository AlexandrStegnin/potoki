package com.art.repository;

import com.art.model.AccountTransaction;
import com.art.model.supporting.dto.AccountDTO;
import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OwnerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT DISTINCT atx.recipient.ownerName FROM AccountTransaction atx ORDER BY atx.recipient.ownerName")
    List<String> getAllRecipients();

    @Query("SELECT DISTINCT atx.payer.ownerName FROM AccountTransaction atx ORDER BY atx.payer.ownerName")
    List<String> getAllPayers();

    @Query("SELECT DISTINCT atx.cashType FROM AccountTransaction atx")
    List<CashType> getAllCashTypes();

    @Query("SELECT DISTINCT atx.owner.ownerName " +
            "FROM AccountTransaction atx " +
            "WHERE atx.owner.ownerType = :ownerType " +
            "ORDER BY atx.owner.ownerName")
    List<String> getOwners(@Param("ownerType") OwnerType ownerType);

    @Query("SELECT new com.art.model.supporting.dto.AccountDTO(atx.owner, SUM(atx.cash)) " +
            "FROM AccountTransaction atx " +
            "WHERE atx.owner.ownerName = :ownerName AND atx.owner.ownerType = :ownerType " +
            "GROUP BY atx.owner")
    Page<AccountDTO> getOwnerSummary(@Param("ownerType") OwnerType ownerType, @Param("ownerName") String ownerName, Pageable pageable);

    @Query("SELECT new com.art.model.supporting.dto.AccountDTO(atx.owner, SUM(atx.cash)) " +
            "FROM AccountTransaction atx " +
            "WHERE atx.owner.ownerType = :ownerType " +
            "AND atx.payer.ownerName = :facilityName " +
            "GROUP BY atx.owner")
    Page<AccountDTO> getSummaryByPayer(@Param("ownerType") OwnerType ownerType,
                                       @Param("facilityName") String facilityName, Pageable pageable);

    @Query("SELECT new com.art.model.supporting.dto.AccountDTO(atx.owner, SUM(atx.cash)) " +
            "FROM AccountTransaction atx " +
            "WHERE atx.owner.ownerName = :ownerName AND atx.owner.ownerType = :ownerType " +
            "AND atx.payer.ownerName = :facilityName " +
            "GROUP BY atx.owner")
    Page<AccountDTO> getSummaryByOwnerAndPayer(@Param("ownerType") OwnerType ownerType, @Param("ownerName") String ownerName,
                                       @Param("facilityName") String facilityName, Pageable pageable);

    @Query("SELECT new com.art.model.supporting.dto.AccountDTO(atx.owner, SUM(atx.cash)) " +
            "FROM AccountTransaction atx " +
            "WHERE atx.owner.ownerType = :ownerType " +
            "GROUP BY atx.owner")
    Page<AccountDTO> getSummary(@Param("ownerType") OwnerType ownerType, Pageable pageable);

    @Query("SELECT new com.art.model.supporting.dto.AccountDTO(atx.owner, SUM(atx.cash)) " +
            "FROM AccountTransaction atx " +
            "WHERE atx.owner.ownerName = :ownerName AND atx.owner.ownerType = :ownerType " +
            "GROUP BY atx.owner")
    AccountDTO getOwnerSummary(@Param("ownerType") OwnerType ownerType, @Param("ownerName") String ownerName);

    List<AccountTransaction> findByOwnerId(Long ownerId);

}
