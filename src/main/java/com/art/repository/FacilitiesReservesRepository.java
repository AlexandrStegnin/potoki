package com.art.repository;

import com.art.model.FacilitiesReserves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FacilitiesReservesRepository extends JpaRepository<FacilitiesReserves, BigInteger>{
}
