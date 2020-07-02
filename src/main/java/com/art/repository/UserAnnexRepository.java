package com.art.repository;

import com.art.model.UsersAnnexToContracts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface UserAnnexRepository extends JpaRepository<UsersAnnexToContracts, Long> {

    Boolean existsByUserIdAndAnnexReadIs(Long userId, Integer read);

    Boolean existsByUserIdAndDateReadIsNull(Long userId);
}
