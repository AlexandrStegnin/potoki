package com.art.repository;

import com.art.model.InvestorsShare;
import com.art.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface InvestorsShareRepository extends JpaRepository<InvestorsShare, String>{
    InvestorsShare findById(BigInteger id);
    void deleteById(BigInteger id);
    List<InvestorsShare> findByInvestor(Users investor);
}
