package com.art.repository;

import com.art.model.InvestorsCash;
import com.art.model.supporting.InvestorsTotalSum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface InvestorsCashRepository extends JpaRepository<InvestorsCash, BigInteger>, JpaSpecificationExecutor<InvestorsCash> {
    InvestorsCash findById(BigInteger id);
    void deleteById(BigInteger id);

    @Query(name = "InvestorsTotalSum", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsTotalSum(BigInteger investorId);

    @Query(name = "InvestorsTotalSumDetails", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsTotalSumDetails(BigInteger investorId);

    @Query(name = "InvestorsCashSums", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsCashSums(BigInteger investorId);

    @Query(name = "InvestorsCashSumsDetails", nativeQuery = true)
    List<InvestorsTotalSum> getInvestorsCashSumsDetails(BigInteger investorId);

    List<InvestorsCash> findByInvestor_Id(BigInteger investorId);

    List<InvestorsCash> findAllByOrderByDateGivedCashAsc();

    List<InvestorsCash> findByRoomId(BigInteger roomId);

    @Query(
            "SELECT ic FROM InvestorsCash ic " +
                    "JOIN ic.investor u " +
                    "JOIN ic.facility f " +
                    "JOIN ic.underFacility uf " +
                    "WHERE (:investors IS NULL OR u.login IN (:investors)) AND " +
                    "(:facility IS NULL OR f.facility = :facility) AND " +
                    "(:underFacility IS NULL OR uf.underFacility = :underFacility) AND " +
                    "(:startDate IS NULL OR ic.dateGivedCash >= :startDate) AND " +
                    "(:endDate IS NULL OR ic.dateGivedCash <= :endDate) "
    )
    Page<InvestorsCash> findFiltering(
            Pageable pageable,
            @Param(value = "investors") List<String> investors,
            @Param(value = "facility") String facility,
            @Param(value = "underFacility") String underFacility,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate
    );

    @Query(
            "SELECT ic FROM InvestorsCash ic " +
                    "JOIN ic.investor u " +
                    "JOIN ic.facility f " +
                    "JOIN ic.underFacility uf " +
                    "WHERE (:investor IS NULL OR u.login = :investor) AND " +
                    "(:facility IS NULL OR f.facility = :facility) AND " +
                    "(:underFacility IS NULL OR uf.underFacility = :underFacility) AND " +
                    "(:startDate IS NULL OR ic.dateGivedCash >= :startDate) AND " +
                    "(:endDate IS NULL OR ic.dateGivedCash <= :endDate) "
    )
    List<InvestorsCash> findFiltering(
            @Param(value = "investor") String investor,
            @Param(value = "facility") String facility,
            @Param(value = "underFacility") String underFacility,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate
    );

    Page<InvestorsCash> findAll(Specification<InvestorsCash> specification, Pageable pageable);

}
