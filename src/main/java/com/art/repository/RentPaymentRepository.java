package com.art.repository;

import com.art.model.RentPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentPaymentRepository extends JpaRepository<RentPayment, Long>, JpaSpecificationExecutor<RentPayment> {

    List<RentPayment> findByInvestorId(Long investorId);

    List<RentPayment> findByIdIn(List<Long> idList);

    List<RentPayment> findByRoomId(Long roomId);

    Page<RentPayment> findAll(Pageable pageable);

    List<RentPayment> findAll();

    Page<RentPayment> findAll(Specification<RentPayment> specification, Pageable pageable);

}
