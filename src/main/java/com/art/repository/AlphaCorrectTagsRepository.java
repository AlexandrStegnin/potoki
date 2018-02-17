package com.art.repository;

import com.art.model.AlphaCorrectTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AlphaCorrectTagsRepository extends JpaRepository<AlphaCorrectTags, BigInteger> {
    AlphaCorrectTags findById(BigInteger id);

    List<AlphaCorrectTags> findByCorrectTagIsNotNull();

    AlphaCorrectTags findByInnAndAccountAndCorrectTag(String inn, String account, String correctTag);

    AlphaCorrectTags findByCorrectTagAndFacilityId(String correctTag, BigInteger facilityId);

    List<AlphaCorrectTags> findByFacilityId(BigInteger facilityId);
}
