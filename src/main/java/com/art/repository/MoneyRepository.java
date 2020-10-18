package com.art.repository;

import com.art.model.Money;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface MoneyRepository extends JpaRepository<Money, Long>, JpaSpecificationExecutor<Money> {

    Money findById(Long id);

    void deleteById(Long id);

    List<Money> findByRoomId(Long roomId);

    Page<Money> findAll(Specification<Money> specification, Pageable pageable);

    List<Money> findByFacilityId(Long facilityId);

    List<Money> findBySourceId(Long sourceId);

    @Query("SELECT m FROM Money m WHERE m.investor.id = :investorId AND m.givenCash = :givenCash AND " +
            "m.facility.id = :facilityId AND DATE(m.dateGiven) = DATE(:dateGiven)")
    List<Money> findDuplicate(@Param("investorId") Long investorId, @Param("givenCash") BigDecimal givenCash,
                        @Param("facilityId") Long facilityId, @Param("dateGiven") Date dateGiven);

    List<Money> findBySourceFlowsId(String sourceId);

    @Query("SELECT m FROM Money m WHERE m.underFacility.id = :underFacilityId AND m.investor.id = :investorId " +
            "AND m.givenCash > 0 AND m.dateClosing IS NULL AND m.typeClosing IS NULL")
    List<Money> getOpenedMonies(@Param("underFacilityId") Long underFacilityId, @Param("investorId") Long investorId);

}
