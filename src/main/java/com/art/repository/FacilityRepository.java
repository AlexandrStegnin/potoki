package com.art.repository;

import com.art.model.Facilities;
import com.art.model.Users;
import com.art.model.supporting.UserFacilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Repository
public interface FacilityRepository extends JpaRepository<Facilities, BigInteger> {
    Facilities findById(BigInteger id);
    Facilities findByFacility(String facility);
    List<Facilities> findAll();
    List<Facilities> findByIdNot(BigInteger id);

    Facilities findByManager(Users manager);

    List<Facilities> findByIdIn(List<BigInteger> idList);

    @Query(name = "InvestorsFacilities", nativeQuery = true)
    List<UserFacilities> getInvestorsFacility(BigInteger rentorInvestorId);

    Facilities findByInvestors(Set<Users> usersSet);

}
