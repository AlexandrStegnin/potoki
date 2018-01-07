package com.art.service;

import com.art.model.*;
import com.art.repository.MainFlowsRepository;
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
public class MainFlowsService {
    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    @Resource(name = "mainFlowsRepository")
    private MainFlowsRepository mainFlowsRepository;

    public MainFlows create(MainFlows mainFlows){
        return mainFlowsRepository.saveAndFlush(mainFlows);
    }

    public List<MainFlows> findAll(){
        return mainFlowsRepository.findAllByOrderBySettlementDateDescUnderFacilitiesAscPaymentAsc();
    }

    public MainFlows findById(BigInteger id){
        return mainFlowsRepository.findOne(id);
    }

    public MainFlows update(MainFlows mainFlows){
        return mainFlowsRepository.saveAndFlush(mainFlows);
    }

    public void deleteById(BigInteger id){
        mainFlowsRepository.delete(id);
    }

    public List<MainFlows> findByIdIn(List<BigInteger> idList){
        return mainFlowsRepository.findByIdIn(idList);
    }

    public void deleteAllFlows(){
        mainFlowsRepository.deleteAll();
    }

    public void createList(List<MainFlows> mainFlowsList){
        mainFlowsRepository.save(mainFlowsList);
    }

    public List<MainFlows> findAllWithCriteriaApi(){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<MainFlows> mainFlowsCriteriaQuery = cb.createQuery(MainFlows.class);
        Root<MainFlows> mainFlowsRoot = mainFlowsCriteriaQuery.from(MainFlows.class);
        /*
        mainFlowsRoot.fetch(MainFlows_.underFacilities, JoinType.LEFT)
                .fetch(UnderFacilities_.rooms, JoinType.LEFT);
        */
        mainFlowsRoot.fetch(MainFlows_.underFacilities, JoinType.LEFT)
                .fetch(UnderFacilities_.facility, JoinType.LEFT)
                .fetch(Facilities_.investors, JoinType.LEFT);
        mainFlowsCriteriaQuery.select(mainFlowsRoot).distinct(true);
        //List<MainFlows> mainFlows = em.createQuery(mainFlowsCriteriaQuery).getResultList();
        //mainFlows.forEach(mf -> mf.getUnderFacilities().getRooms().size());
        return em.createQuery(mainFlowsCriteriaQuery).getResultList();
    }

    /*
    public List<MainFlows> findByInvestorId(BigInteger investorId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MainFlows> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.and(cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId),
                cb.equal(investorsCashRoot.get(InvestorsCash_.facilityId), facilityId),
                cb.gt(investorsCashRoot.get(InvestorsCash_.givedCash), 0),
                cb.isNull(investorsCashRoot.get(InvestorsCash_.typeClosingInvest))));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }
    */
}
