package com.art.repository;

import com.art.model.MarketingTree;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Transactional
public interface MarketingTreeRepository {

    List<MarketingTree> findAll();

    void updateMarketingTree(BigInteger invId);
}
