package com.art.repository;

import com.art.model.FacilitiesServiceContracts;
import com.art.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface FacilitiesServiceContractsRepository
        extends JpaRepository<FacilitiesServiceContracts, BigInteger>{
    FacilitiesServiceContracts findById(BigInteger id);
    List<FacilitiesServiceContracts> findByRentor(Users user);
}
