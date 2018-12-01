package com.art.repository;

import com.art.model.InvestorsFlows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface InvestorsFlowsRepository extends PagingAndSortingRepository<InvestorsFlows, BigInteger> {
    List<InvestorsFlows> findByInvestorId(BigInteger investorId);

    List<InvestorsFlows> findByIdIn(List<BigInteger> idList);

    List<InvestorsFlows> findByRoomId(BigInteger roomId);

    Page<InvestorsFlows> findAll(Pageable pageable);

    @Query(
            "SELECT fl FROM InvestorsFlows fl " +
                    "JOIN fl.investor u " +
                    "JOIN fl.facility f " +
                    "JOIN fl.underFacilities uf " +
                    "WHERE (:investor IS NULL OR u.login = :investor) AND " +
                    "(:facility IS NULL OR f.facility = :facility) AND " +
                    "(:underFacility IS NULL OR uf.underFacility = :underFacility) AND " +
                    "(:startDate IS NULL OR fl.reportDate >= :startDate) AND " +
                    "(:endDate IS NULL OR fl.reportDate <= :endDate) "
    )
    Page<InvestorsFlows> findFiltering(
            Pageable pageable,
            @Param(value = "investor") String investor,
            @Param(value = "facility") String facility,
            @Param(value = "underFacility") String underFacility,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate
    );

    List<InvestorsFlows> findAll();
}
