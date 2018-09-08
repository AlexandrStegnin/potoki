package com.art.repository;

import com.art.model.AllowanceIp;
import com.art.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AllowanceIpRepository extends JpaRepository<AllowanceIp, BigInteger> {
    List<AllowanceIp> findByFacilityId(BigInteger facilityId);

    List<AllowanceIp> findByInvestor(Users user);
}
