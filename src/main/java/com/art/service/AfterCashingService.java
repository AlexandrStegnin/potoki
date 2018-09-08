package com.art.service;

import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.AfterCashing_;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class AfterCashingService {
    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public List<AfterCashing> findByOldId(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AfterCashing> afterCashingCriteriaQuery = cb.createQuery(AfterCashing.class);
        Root<AfterCashing> afterCashingRoot = afterCashingCriteriaQuery.from(AfterCashing.class);
        afterCashingCriteriaQuery.select(afterCashingRoot);
        afterCashingCriteriaQuery.where(cb.equal(afterCashingRoot.get(AfterCashing_.oldId), id));
        return em.createQuery(afterCashingCriteriaQuery).getResultList();
    }

    public void create(AfterCashing afterCashing) {
        this.em.merge(afterCashing);
    }

    public void deleteById(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<AfterCashing> delete = cb.createCriteriaDelete(AfterCashing.class);
        Root<AfterCashing> afterCashingRoot = delete.from(AfterCashing.class);
        delete.where(cb.equal(afterCashingRoot.get(AfterCashing_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public List<AfterCashing> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AfterCashing> afterCashingCriteriaQuery = cb.createQuery(AfterCashing.class);
        Root<AfterCashing> afterCashingRoot = afterCashingCriteriaQuery.from(AfterCashing.class);
        afterCashingCriteriaQuery.select(afterCashingRoot);
        return em.createQuery(afterCashingCriteriaQuery).getResultList();
    }
}
