package com.art.repository;

import com.art.model.Account;
import com.art.model.supporting.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(String accountNumber);

    Account findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType);

    boolean existsByAccountNumber(String accountNumber);

}
