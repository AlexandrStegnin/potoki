package com.art.repository;

import com.art.model.MainFlows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface MainFlowsRepository extends JpaRepository<MainFlows, BigInteger> {
    List<MainFlows> findByIdIn(List<BigInteger> idList);

    List<MainFlows> findAllByOrderBySettlementDateDescUnderFacilitiesAscPaymentAsc();

}
