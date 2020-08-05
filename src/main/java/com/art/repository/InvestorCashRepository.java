package com.art.repository;

import com.art.model.InvestorCash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorCashRepository extends JpaRepository<InvestorCash, Long>, JpaSpecificationExecutor<InvestorCash> {

    InvestorCash findById(Long id);

    void deleteById(Long id);

    List<InvestorCash> findByRoomId(Long roomId);

    Page<InvestorCash> findAll(Specification<InvestorCash> specification, Pageable pageable);

    List<InvestorCash> findByFacilityId(Long facilityId);

    List<InvestorCash> findBySourceId(Long sourceId);

}
