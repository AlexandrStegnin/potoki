package com.art.repository;

import com.art.model.Bonuses;
import com.art.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface BonusesRepository extends JpaRepository<Bonuses, BigInteger> {
    List<Bonuses> findByManager(Users manager);

    List<Bonuses> findByRentor(Users rentor);

}
