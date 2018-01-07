package com.art.repository;

import com.art.model.Stuffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface StuffRepository extends JpaRepository<Stuffs, BigInteger> {
    Stuffs findById(BigInteger id);
    //Stuffs findByUserId(BigInteger userId);
    //Stuffs findByUserIdIsNull();
    Stuffs findByStuff(String stuff);
}
