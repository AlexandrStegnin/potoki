package com.art.service;

import com.art.model.*;
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

    public InvestorsCash findById(BigInteger id){
        return investorsCashRepository.findById(id);
    }

    public InvestorsCash update(InvestorsCash investorsCash){
        return investorsCashRepository.saveAndFlush(investorsCash);
    }

    public void deleteById(BigInteger id){
        investorsCashRepository.deleteById(id);
    }

    /*
    public void create(InvestorsCash investorsCash){
        investorsCashRepository.save(investorsCash);
    }
    */


    public List<InvestorsTotalSum> getInvestorsTotalSum(BigInteger investorId){
        return investorsCashRepository.getInvestorsTotalSum(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsTotalSumDetails(BigInteger investorId){
        return investorsCashRepository.getInvestorsTotalSumDetails(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsCashSums(BigInteger investorId){
        return investorsCashRepository.getInvestorsCashSums(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsCashSumsDetails(BigInteger investorId){
        return investorsCashRepository.getInvestorsCashSumsDetails(investorId);
    }

    public List<InvestorsCash> findByInvestorId(BigInteger investorId){
        return investorsCashRepository.findByInvestor_Id(investorId);
    }

    public List<InvestorsCash> findAllOrderByDateGivedCashAsc(){
        return investorsCashRepository.findAllByOrderByDateGivedCashAsc();
    }

    public void saveAll(List<InvestorsCash> investorsCashes){
        investorsCashRepository.save(investorsCashes);
    }


    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public void create(InvestorsCash investorsCash){
        this.em.merge(investorsCash);
    }

    public List<InvestorsCash> findByIdIn(List<BigInteger> idList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(investorsCashRoot.get(InvestorsCash_.id).in(idList));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findBySourceId(BigInteger sourceId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(investorsCashRoot.get(InvestorsCash_.sourceId).in(sourceId));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorAndFacility(BigInteger investorId, BigInteger facilityId){
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

}
