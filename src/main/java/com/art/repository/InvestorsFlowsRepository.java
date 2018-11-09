package com.art.repository;

import com.art.model.InvestorsFlows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface InvestorsFlowsRepository extends JpaRepository<InvestorsFlows, String> {
    List<InvestorsFlows> findByInvestorId(BigInteger investorId);

    List<InvestorsFlows> findByIdIn(List<BigInteger> idList);

    List<InvestorsFlows> findByRoomId(BigInteger roomId);
}
