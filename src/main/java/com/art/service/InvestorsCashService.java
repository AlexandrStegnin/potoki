package com.art.service;

import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.InvestorsTotalSum;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.filters.CashFilter;
import com.art.repository.InvestorsCashRepository;
import com.art.specifications.InvestorsCashSpecification;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class InvestorsCashService {

    private final InvestorsCashRepository investorsCashRepository;
    private final InvestorsCashSpecification specification;
    private final TypeClosingInvestService typeClosingInvestService;
    private final AfterCashingService afterCashingService;

    @Autowired
    public InvestorsCashService(InvestorsCashRepository investorsCashRepository,
                                InvestorsCashSpecification specification,
                                TypeClosingInvestService typeClosingInvestService,
                                AfterCashingService afterCashingService) {
        this.investorsCashRepository = investorsCashRepository;
        this.specification = specification;
        this.typeClosingInvestService = typeClosingInvestService;
        this.afterCashingService = afterCashingService;
    }

    public List<InvestorsCash> findAll() {
        return investorsCashRepository.findAll();
    }

    public InvestorsCash findById(BigInteger id) {
        return investorsCashRepository.findById(id);
    }

    public InvestorsCash update(InvestorsCash investorsCash) {
        return investorsCashRepository.saveAndFlush(investorsCash);
    }

    public void deleteById(BigInteger id) {
        investorsCashRepository.deleteById(id);
    }

    public List<InvestorsTotalSum> getInvestorsCashSums(BigInteger investorId) {
        return investorsCashRepository.getInvestorsCashSums(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsCashSumsDetails(BigInteger investorId) {
        return investorsCashRepository.getInvestorsCashSumsDetails(investorId);
    }

    public void saveAll(List<InvestorsCash> investorsCashes) {
        investorsCashRepository.save(investorsCashes);
    }

    public List<InvestorsCash> findByRoomId(BigInteger roomId) {
        return investorsCashRepository.findByRoomId(roomId);
    }

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public InvestorsCash create(InvestorsCash investorsCash) {
        InvestorsCash cash = this.em.merge(investorsCash);
        return cash;
    }

    public List<InvestorsCash> findByIdIn(List<BigInteger> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(investorsCashRoot.get(InvestorsCash_.id).in(idList));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findBySource(String source) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.source), source));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findBySourceId(BigInteger sourceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.sourceId), sourceId));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorAndFacility(BigInteger investorId, BigInteger facilityId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.and(cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId),
                cb.equal(investorsCashRoot.get(InvestorsCash_.facilityId), facilityId),
                cb.gt(investorsCashRoot.get(InvestorsCash_.givedCash), 0),
                cb.isNull(investorsCashRoot.get(InvestorsCash_.typeClosingInvest))));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorId(BigInteger investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.underFacility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorsCash_.dateGivedCash)));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public Facilities getSumCash(BigInteger investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.multiselect(investorsCashRoot.get(InvestorsCash_.facility),
                cb.sum(investorsCashRoot.get(InvestorsCash_.givedCash)));
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.investor).get(Users_.id), investorId));
        investorsCashCriteriaQuery.groupBy(investorsCashRoot.get(InvestorsCash_.facility));
        List<InvestorsCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        final Facilities[] facility = {new Facilities()};
        final BigDecimal[] max = {new BigDecimal(BigInteger.ZERO)};
        cash.forEach(c -> {
            c.setGivedCash(c.getGivedCash().stripTrailingZeros());
            if (c.getGivedCash().compareTo(max[0]) > 0) {
                max[0] = c.getGivedCash();
                facility[0] = c.getFacility();
            }
        });
        return facility[0];
    }

    public List<InvestorsCash> findAllWithAllFields() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.facility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.isNotNull(investorsCashRoot.get(InvestorsCash_.facility)));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorsCash_.dateGivedCash)));
        List<InvestorsCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        cash.forEach(c -> {
            Hibernate.initialize(c.getInvestor());
            Hibernate.initialize(c.getFacility());
            Hibernate.initialize(c.getUnderFacility());
            Hibernate.initialize(c.getRoom());
        });

        return cash;
    }

    public Page<InvestorsCash> findAll(CashFilter filters, Pageable pageable) {
        return investorsCashRepository.findAll(
                specification.getFilter(filters),
                pageable
        );
    }
}
