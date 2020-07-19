package com.art.repository;

import com.art.model.CashSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashSourceRepository extends JpaRepository<CashSource, Long> {

    CashSource findByName(String name);

}
