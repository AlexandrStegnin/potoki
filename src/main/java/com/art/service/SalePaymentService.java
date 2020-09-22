package com.art.service;

import com.art.model.SalePayment;
import com.art.model.SalePayment_;
import com.art.model.supporting.filters.FlowsSaleFilter;
import com.art.repository.SalePaymentRepository;
import com.art.specifications.SalePaymentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SalePaymentService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final SalePaymentRepository saleRepository;

    private final SalePaymentSpecification saleSpecification;

    @Autowired
    public SalePaymentService(SalePaymentRepository saleRepository,
                              SalePaymentSpecification saleSpecification) {
        this.saleRepository = saleRepository;
        this.saleSpecification = saleSpecification;
    }

//    @CachePut(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void create(SalePayment sale) {
        saleRepository.save(sale);
    }

//    @CachePut(value = Constant.INVESTOR_FLOWS_SALE_CACHE_KEY, key = "#sale.id")
    public void update(SalePayment sale) {
        this.em.merge(sale);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> saleCriteriaQuery = cb.createQuery(SalePayment.class);
        Root<SalePayment> saleRoot = saleCriteriaQuery.from(SalePayment.class);
        saleRoot.fetch(SalePayment_.facility, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.investor, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findByIdInWithAllFields(List<Long> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> saleCriteriaQuery = cb.createQuery(SalePayment.class);
        Root<SalePayment> saleRoot = saleCriteriaQuery.from(SalePayment.class);
        saleRoot.fetch(SalePayment_.facility, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.investor, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(saleRoot.get(SalePayment_.id).in(idList));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findByIdIn(List<Long> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> saleCriteriaQuery = cb.createQuery(SalePayment.class);
        Root<SalePayment> saleRoot = saleCriteriaQuery.from(SalePayment.class);
        saleCriteriaQuery.select(saleRoot);
        saleCriteriaQuery.where(saleRoot.get(SalePayment_.id).in(idList));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public SalePayment findById(BigInteger id) {
        return this.em.find(SalePayment.class, id);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void delete() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<SalePayment> query = cb.createCriteriaDelete(SalePayment.class);
        query.from(SalePayment.class);
        em.createQuery(query).executeUpdate();
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void deleteById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<SalePayment> delete = cb.createCriteriaDelete(SalePayment.class);
        Root<SalePayment> flowsSaleRoot = delete.from(SalePayment.class);
        delete.where(cb.equal(flowsSaleRoot.get(SalePayment_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findBySourceId(BigInteger sourceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> saleCriteriaQuery = cb.createQuery(SalePayment.class);
        Root<SalePayment> saleRoot = saleCriteriaQuery.from(SalePayment.class);
        saleRoot.fetch(SalePayment_.facility, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.investor, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(cb.equal(saleRoot.get(SalePayment_.sourceId), sourceId));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    public SalePayment findParentFlow(SalePayment flowsSale, List<SalePayment> childFlows) {
        if (!Objects.equals(null, flowsSale.getSourceId())) {
            childFlows.add(flowsSale);
            SalePayment finalFlowsSale = flowsSale;
            flowsSale = findById(finalFlowsSale.getSourceId());
            return findParentFlow(flowsSale, childFlows);
        }
        return flowsSale;
    }

    public List<SalePayment> findAllChildes(SalePayment parentFlow, List<SalePayment> childFlows, int next) {
        List<SalePayment> tmp = findBySourceId(parentFlow.getId());
        tmp.forEach(t -> {
            if (!childFlows.contains(t)) {
                childFlows.add(t);
            }
        });
        if (next >= childFlows.size()) next--;
        if (next < 0) return childFlows;
        SalePayment parent = childFlows.get(next);
        if (parentFlow.getId().equals(parent.getId())) return childFlows;
        next++;
        return findAllChildes(parent, childFlows, next);

    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public Page<SalePayment> findAll(FlowsSaleFilter filters, Pageable pageable) {
        if (filters.getPageSize() == 0) pageable = new PageRequest(filters.getPageNumber(), filters.getTotal() + 1);
        return saleRepository.findAll(
                saleSpecification.getFilter(filters),
                pageable
        );
    }
}
