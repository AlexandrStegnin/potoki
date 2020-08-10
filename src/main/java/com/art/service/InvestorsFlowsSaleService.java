package com.art.service;

import com.art.config.application.Constant;
import com.art.model.InvestorsFlowsSale;
import com.art.model.InvestorsFlowsSale_;
import com.art.model.supporting.filters.FlowsSaleFilter;
import com.art.repository.InvestorsFlowsSaleRepository;
import com.art.specifications.InvestorsFlowsSaleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class InvestorsFlowsSaleService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final InvestorsFlowsSaleRepository saleRepository;

    private final InvestorsFlowsSaleSpecification saleSpecification;

    @Autowired
    public InvestorsFlowsSaleService(InvestorsFlowsSaleRepository saleRepository,
                                     InvestorsFlowsSaleSpecification saleSpecification) {
        this.saleRepository = saleRepository;
        this.saleSpecification = saleSpecification;
    }

    @CachePut(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void create(InvestorsFlowsSale sale) {
        saleRepository.save(sale);
    }

    @CachePut(value = Constant.INVESTOR_FLOWS_SALE_CACHE_KEY, key = "#sale.id")
    public void update(InvestorsFlowsSale sale) {
        this.em.merge(sale);
    }

    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<InvestorsFlowsSale> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleRoot.fetch(InvestorsFlowsSale_.facility, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.investor, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<InvestorsFlowsSale> findByIdInWithAllFields(List<Long> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleRoot.fetch(InvestorsFlowsSale_.facility, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.investor, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(saleRoot.get(InvestorsFlowsSale_.id).in(idList));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<InvestorsFlowsSale> findByIdIn(List<Long> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleCriteriaQuery.select(saleRoot);
        saleCriteriaQuery.where(saleRoot.get(InvestorsFlowsSale_.id).in(idList));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public InvestorsFlowsSale findById(BigInteger id) {
        return this.em.find(InvestorsFlowsSale.class, id);
    }

    @CacheEvict(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void delete() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<InvestorsFlowsSale> query = cb.createCriteriaDelete(InvestorsFlowsSale.class);
        query.from(InvestorsFlowsSale.class);
        em.createQuery(query).executeUpdate();
    }

    @CacheEvict(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void deleteById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<InvestorsFlowsSale> delete = cb.createCriteriaDelete(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> flowsSaleRoot = delete.from(InvestorsFlowsSale.class);
        delete.where(cb.equal(flowsSaleRoot.get(InvestorsFlowsSale_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<InvestorsFlowsSale> findBySourceId(BigInteger sourceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleRoot.fetch(InvestorsFlowsSale_.facility, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.investor, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(cb.equal(saleRoot.get(InvestorsFlowsSale_.sourceId), sourceId));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    public InvestorsFlowsSale findParentFlow(InvestorsFlowsSale flowsSale, List<InvestorsFlowsSale> childFlows) {
        if (!Objects.equals(null, flowsSale.getSourceId())) {
            childFlows.add(flowsSale);
            InvestorsFlowsSale finalFlowsSale = flowsSale;
            flowsSale = findById(finalFlowsSale.getSourceId());
            return findParentFlow(flowsSale, childFlows);
        }
        return flowsSale;
    }

    public List<InvestorsFlowsSale> findAllChildes(InvestorsFlowsSale parentFlow, List<InvestorsFlowsSale> childFlows, int next) {
        List<InvestorsFlowsSale> tmp = findBySourceId(parentFlow.getId());
        tmp.forEach(t -> {
            if (!childFlows.contains(t)) {
                childFlows.add(t);
            }
        });
        if (next >= childFlows.size()) next--;
        if (next < 0) return childFlows;
        InvestorsFlowsSale parent = childFlows.get(next);
        if (parentFlow.getId().equals(parent.getId())) return childFlows;
        next++;
        return findAllChildes(parent, childFlows, next);

    }

    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public Page<InvestorsFlowsSale> findAll(FlowsSaleFilter filters, Pageable pageable) {
        if (filters.getPageSize() == 0) pageable = new PageRequest(filters.getPageNumber(), filters.getTotal() + 1);
        return saleRepository.findAll(
                saleSpecification.getFilter(filters),
                pageable
        );
    }
}
