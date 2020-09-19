package com.art.repository;

import com.art.model.RentPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RentPaymentRepository extends JpaRepository<RentPayment, Long>, JpaSpecificationExecutor<RentPayment> {

    List<RentPayment> findByInvestorId(Long investorId);

    List<RentPayment> findByIdIn(List<Long> idList);

    List<RentPayment> findByRoomId(Long roomId);

    Page<RentPayment> findAll(Pageable pageable);

    @Query(
            "SELECT fl FROM RentPayment fl " +
                    "JOIN fl.investor u " +
                    "JOIN fl.facility f " +
                    "JOIN fl.underFacility uf " +
                    "WHERE (:investor IS NULL OR u.login = :investor) AND " +
                    "(:name IS NULL OR f.name = :name) AND " +
                    "(:underFacility IS NULL OR uf.name = :underFacility) AND " +
                    "(:startDate IS NULL OR fl.reportDate >= :startDate) AND " +
                    "(:endDate IS NULL OR fl.reportDate <= :endDate) "
    )
    Page<RentPayment> findFiltering(
            Pageable pageable,
            @Param(value = "investor") String investor,
            @Param(value = "name") String name,
            @Param(value = "underFacility") String underFacility,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate
    );

    List<RentPayment> findAll();

    Page<RentPayment> findAll(Specification<RentPayment> specification, Pageable pageable);

}
