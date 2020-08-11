package com.art.repository;

import com.art.model.Money;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyRepository extends JpaRepository<Money, Long>, JpaSpecificationExecutor<Money> {

    Money findById(Long id);

    void deleteById(Long id);

    List<Money> findByRoomId(Long roomId);

    Page<Money> findAll(Specification<Money> specification, Pageable pageable);

    List<Money> findByFacilityId(Long facilityId);

    List<Money> findBySourceId(Long sourceId);

}
