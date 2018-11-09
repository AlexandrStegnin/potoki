package com.art.repository;

import com.art.model.MarketingTree;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Transactional
public interface MarketingTreeRepository {

    boolean save(final MarketingTree marketingTree);

    boolean update(final MarketingTree marketingTree);

    boolean calculate(String login);

    List<MarketingTree> findAll();

    MarketingTree findByInvestorId(BigInteger investorId);

    void removeByInvestorId(BigInteger id);

    int getSerialNumber(BigInteger partnerId, Date firstInvestmentDate);

    List<MarketingTree> findByPartnerIdAndFirstInvestmentDate(BigInteger partnerId, Date firstInvestmentDate);
}
