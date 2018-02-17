package com.art.service;

import com.art.model.*;
import com.art.repository.InvestorsFlowsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class InvestorsFlowsService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    @Resource(name = "investorsFlowsRepository")
    private
    InvestorsFlowsRepository investorsFlowsRepository;

    public List<InvestorsFlows> findAll() {
        return investorsFlowsRepository.findAll();
    }

    public void delete() {
        investorsFlowsRepository.deleteAll();
    }

    public void saveList(List<InvestorsFlows> investorsFlowsList) {
        investorsFlowsRepository.save(investorsFlowsList);
    }

    public List<InvestorsFlows> findByInvestorId(BigInteger investorId) {
        return investorsFlowsRepository.findByInvestorId(investorId);
    }

    public List<InvestorsFlows> findByIdIn(List<BigInteger> idList) {
        return investorsFlowsRepository.findByIdIn(idList);
    }

    public InvestorsFlows findById(BigInteger id) {
        return this.em.find(InvestorsFlows.class, id);
    }

    public void update(InvestorsFlows flows) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<InvestorsFlows> update = criteriaBuilder.createCriteriaUpdate(InvestorsFlows.class);
        Root<InvestorsFlows> flowsRoot = update.from(InvestorsFlows.class);
        update.set(InvestorsFlows_.isReinvest, flows.getIsReinvest());
        update.where(criteriaBuilder.equal(flowsRoot.get(InvestorsFlows_.id), flows.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public void deleteByIdIn(List<BigInteger> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<InvestorsFlows> query = cb.createCriteriaDelete(InvestorsFlows.class);
        Root<InvestorsFlows> root = query.from(InvestorsFlows.class);
        query.where(root.get(InvestorsFlows_.id).in(idList));
        em.createQuery(query).executeUpdate();
    }

    public List<InvestorsFlows> findByInvestorIdWithFacilitiesUnderFacilities(BigInteger investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlows> flowsCriteriaQuery = cb.createQuery(InvestorsFlows.class);
        Root<InvestorsFlows> flowsRoot = flowsCriteriaQuery.from(InvestorsFlows.class);
        flowsRoot.fetch(InvestorsFlows_.facility, JoinType.LEFT);
        //.fetch(Facilities_.underFacilities, JoinType.LEFT);
        flowsCriteriaQuery.select(flowsRoot).distinct(true);
        flowsCriteriaQuery.where(cb.equal(flowsRoot.get(InvestorsFlows_.investor).get(Users_.id), investorId));
        return em.createQuery(flowsCriteriaQuery).getResultList();
    }
}
