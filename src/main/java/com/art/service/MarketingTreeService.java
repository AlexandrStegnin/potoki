package com.art.service;

import com.art.model.MarketingTree;
import com.art.model.supporting.filters.MarketingTreeFilter;
import com.art.repository.MarketingTreeRepository;
import com.art.specifications.MarketingTreeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class MarketingTreeService {

    @PersistenceContext(name = "persistanceUnit")
    EntityManager em;

    private final MarketingTreeSpecification specification;
    private final MarketingTreeRepository marketingTreeRepository;

    @Autowired
    public MarketingTreeService(MarketingTreeSpecification specification, MarketingTreeRepository marketingTreeRepository) {
        this.specification = specification;
        this.marketingTreeRepository = marketingTreeRepository;
    }

    public List<MarketingTree> findAll() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<MarketingTree> marketingTreeCriteriaQuery = cb.createQuery(MarketingTree.class);
        final Root<MarketingTree> marketingTreeRoot = marketingTreeCriteriaQuery.from(MarketingTree.class);
        marketingTreeCriteriaQuery.select(marketingTreeRoot);
        return em.createQuery(marketingTreeCriteriaQuery).getResultList();
    }

    public void updateMarketingTree(BigInteger invId){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("updateMarketingTree");
        query.setParameter("invId", invId);
        query.execute();
    }

    public Page<MarketingTree> findAll(MarketingTreeFilter filters, Pageable pageable) {
        return marketingTreeRepository.findAll(
                specification.getFilter(filters),
                pageable
        );
    }
}
