package com.art.repository;

import com.art.model.AlphaCorrectTags;
import com.art.model.AlphaExtract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AlphaExtractRepository extends JpaRepository<AlphaExtract, BigInteger> {
    List<AlphaExtract> findByInnOrAccountOrPurposePaymentContaining(
            String inn, String account, String description);

    void deleteAllByPIdIsNull();

    List<AlphaExtract> findByTagsIsNotNull();

    List<AlphaExtract> findByTags(AlphaCorrectTags tags);

}
