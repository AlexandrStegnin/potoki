package com.art.repository;

import com.art.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Facility findByName(String facility);

    @Query("SELECT DISTINCT(m.facility) FROM Money m " +
            "WHERE m.givenCash > 0 " +
            "AND m.investor.id = :investorId " +
            "AND m.dateClosing IS NULL " +
            "AND m.typeClosing IS NULL")
    List<Facility> findOpenedProjects(@Param("investorId") Long investorId);

}
