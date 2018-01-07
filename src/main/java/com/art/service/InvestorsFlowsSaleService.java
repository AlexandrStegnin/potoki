package com.art.service;

import com.art.model.InvestorsFlowsSale;
import com.art.model.InvestorsFlowsSale_;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class InvestorsFlowsSaleService {
    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public InvestorsFlowsSale findWithAllFields(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleRoot.fetch(InvestorsFlowsSale_.facility, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.investor, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.shareKind, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(cb.equal(saleRoot.get(InvestorsFlowsSale_.id), id));
        return em.createQuery(saleCriteriaQuery).getSingleResult();
    }

    public void create(InvestorsFlowsSale sale){
        this.em.persist(sale);
    }

    public void update(InvestorsFlowsSale sale){
        this.em.merge(sale);
    }

    public List<InvestorsFlowsSale> findAll(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleRoot.fetch(InvestorsFlowsSale_.facility, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.investor, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.shareKind, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    public List<InvestorsFlowsSale> findByIdInWithAllFields(List<BigInteger> idList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleRoot.fetch(InvestorsFlowsSale_.facility, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.investor, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.shareKind, JoinType.LEFT);
        saleRoot.fetch(InvestorsFlowsSale_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(saleRoot.get(InvestorsFlowsSale_.id).in(idList));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    public List<InvestorsFlowsSale> findByIdIn(List<BigInteger> idList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlowsSale> saleCriteriaQuery = cb.createQuery(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> saleRoot = saleCriteriaQuery.from(InvestorsFlowsSale.class);
        saleCriteriaQuery.select(saleRoot);
        saleCriteriaQuery.where(saleRoot.get(InvestorsFlowsSale_.id).in(idList));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    public void deleteByIdIn(List<BigInteger> idList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<InvestorsFlowsSale> query = cb.createCriteriaDelete(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> root = query.from(InvestorsFlowsSale.class);
        query.where(root.get(InvestorsFlowsSale_.id).in(idList));
        em.createQuery(query).executeUpdate();
    }

    public InvestorsFlowsSale findById(BigInteger id){
        return this.em.find(InvestorsFlowsSale.class, id);
    }

    public void delete(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<InvestorsFlowsSale> query = cb.createCriteriaDelete(InvestorsFlowsSale.class);
        query.from(InvestorsFlowsSale.class);
        em.createQuery(query).executeUpdate();
    }

    public void deleteById(BigInteger id){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<InvestorsFlowsSale> delete = cb.createCriteriaDelete(InvestorsFlowsSale.class);
        Root<InvestorsFlowsSale> flowsSaleRoot = delete.from(InvestorsFlowsSale.class);
        delete.where(cb.equal(flowsSaleRoot.get(InvestorsFlowsSale_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }
}
