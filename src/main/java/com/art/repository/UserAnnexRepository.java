package com.art.repository;

import com.art.model.UsersAnnexToContracts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface UserAnnexRepository extends JpaRepository<UsersAnnexToContracts, Long> {

    Boolean existsByUserIdAndAnnexReadIs(BigInteger userId, Integer read);

    Boolean existsByUserIdAndDateReadIsNull(BigInteger userId);
}
