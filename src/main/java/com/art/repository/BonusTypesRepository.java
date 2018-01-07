package com.art.repository;

import com.art.model.BonusTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface BonusTypesRepository extends JpaRepository<BonusTypes, BigInteger>{
    BonusTypes findByBonusType(String bonusType);
}
