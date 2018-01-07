package com.art.repository;

import com.art.model.ToshlCorrectTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ToshlCorrectTagsRepository extends JpaRepository<ToshlCorrectTags, BigInteger>{
    List<ToshlCorrectTags> findByCorrectTagNotNullOrderByDateStTag();
}
