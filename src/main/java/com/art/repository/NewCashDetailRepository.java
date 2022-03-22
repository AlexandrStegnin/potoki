package com.art.repository;

import com.art.model.NewCashDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCashDetailRepository extends JpaRepository<NewCashDetail, Long> {

  NewCashDetail findByName(String name);
}
