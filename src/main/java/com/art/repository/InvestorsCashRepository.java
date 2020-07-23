package com.art.repository;

import com.art.model.InvestorsCash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorsCashRepository extends JpaRepository<InvestorsCash, Long>, JpaSpecificationExecutor<InvestorsCash> {

    InvestorsCash findById(Long id);

    void deleteById(Long id);

    List<InvestorsCash> findByRoomId(Long roomId);

    Page<InvestorsCash> findAll(Specification<InvestorsCash> specification, Pageable pageable);

    List<InvestorsCash> findByFacilityId(Long facilityId);
}
