package com.art.repository;

import com.art.model.MailingGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface MailingGroupsRepository extends JpaRepository<MailingGroups, BigInteger> {
    List<MailingGroups> findByIdIn(List<BigInteger> idList);
}
