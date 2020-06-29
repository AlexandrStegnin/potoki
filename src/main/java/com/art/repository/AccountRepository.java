package com.art.repository;

import com.art.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(Long accountNumber);

}
