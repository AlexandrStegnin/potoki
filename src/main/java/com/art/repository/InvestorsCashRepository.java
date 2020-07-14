package com.art.repository;

import com.art.model.InvestorsCash;
import com.art.model.supporting.InvestorsTotalSum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface InvestorsCashRepository extends JpaRepository<InvestorsCash, BigInteger>, JpaSpecificationExecutor<InvestorsCash> {
    InvestorsCash findById(BigInteger id);
    void deleteById(BigInteger id);

    @Query(name = "InvestorsCashSums", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsCashSums(Long investorId);

    List<InvestorsCash> findByRoomId(BigInteger roomId);

    Page<InvestorsCash> findAll(Specification<InvestorsCash> specification, Pageable pageable);

    List<InvestorsCash> findByFacilityId(Long facilityId);
}
