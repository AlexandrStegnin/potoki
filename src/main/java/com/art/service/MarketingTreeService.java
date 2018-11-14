package com.art.service;

import com.art.model.MarketingTree;
import com.art.repository.MarketingTreeRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;

@Service
public class MarketingTreeService implements MarketingTreeRepository {

    @PersistenceContext(name = "persistanceUnit")
    EntityManager em;

    @Override
    public List<MarketingTree> findAll() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<MarketingTree> marketingTreeCriteriaQuery = cb.createQuery(MarketingTree.class);
        final Root<MarketingTree> marketingTreeRoot = marketingTreeCriteriaQuery.from(MarketingTree.class);
        marketingTreeCriteriaQuery.select(marketingTreeRoot);
        return em.createQuery(marketingTreeCriteriaQuery).getResultList();
    }

    @Override
    public void updateMarketingTree(BigInteger invId){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("updateMarketingTree");
        query.setParameter("invId", invId);
        query.execute();
    }
}
