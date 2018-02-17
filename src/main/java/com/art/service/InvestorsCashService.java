package com.art.service;

import com.art.model.Facilities;
import com.art.model.InvestorsCash;
import com.art.model.InvestorsCash_;
import com.art.model.Users_;
import com.art.model.supporting.InvestorsTotalSum;
import com.art.repository.InvestorsCashRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class InvestorsCashService {

    @Resource(name = "investorsCashRepository")
    private InvestorsCashRepository investorsCashRepository;

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


    public List<InvestorsTotalSum> getInvestorsTotalSum(BigInteger investorId) {
        return investorsCashRepository.getInvestorsTotalSum(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsTotalSumDetails(BigInteger investorId) {
        return investorsCashRepository.getInvestorsTotalSumDetails(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsCashSums(BigInteger investorId) {
        return investorsCashRepository.getInvestorsCashSums(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsCashSumsDetails(BigInteger investorId) {
        return investorsCashRepository.getInvestorsCashSumsDetails(investorId);
    }

    public List<InvestorsCash> findAllOrderByDateGivedCashAsc() {
        return investorsCashRepository.findAllByOrderByDateGivedCashAsc();
    }

    public void saveAll(List<InvestorsCash> investorsCashes) {
        investorsCashRepository.save(investorsCashes);
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

    public List<InvestorsCash> findBySourceId(BigInteger sourceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(investorsCashRoot.get(InvestorsCash_.sourceId).in(sourceId));
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
        //investorsCashCriteriaQuery.orderBy(cb.desc(investorsCashRoot.get(InvestorsCash_.givedCash)));
        List<InvestorsCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        final Facilities[] facility = {new Facilities()};
        final BigDecimal[] max = {new BigDecimal(BigInteger.ZERO)};
        cash.forEach(c -> {
            if (c.getGivedCash().compareTo(max[0]) > 0) {
                max[0] = c.getGivedCash();
                facility[0] = c.getFacility();
            }
        });
        return facility[0];
    }

}
