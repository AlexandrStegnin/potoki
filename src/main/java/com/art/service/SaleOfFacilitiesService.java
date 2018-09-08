package com.art.service;

import com.art.model.SaleOfFacilities;
import com.art.model.SaleOfFacilities_;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Service
@Transactional
@Repository
public class SaleOfFacilitiesService {
    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public void createList(List<SaleOfFacilities> saleOfFacilities) {
        saleOfFacilities.forEach(sof -> em.persist(sof));
    }

    public void deleteAll(){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // create delete
        CriteriaDelete<SaleOfFacilities> delete = cb.
                createCriteriaDelete(SaleOfFacilities.class);

        // set the root class
        Root saleOfFacilitiesRoot = delete.from(SaleOfFacilities.class);

        // set where clause
        //delete.where(cb.lessThanOrEqualTo(saleOfFacilitiesRoot.get(SaleOfFacilities_.id), new BigDecimal("0")));

        // perform update
        this.em.createQuery(delete).executeUpdate();
    }

    public List<SaleOfFacilities> findAll(){
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<SaleOfFacilities> saleOfFacilitiesCriteriaQuery = cb.createQuery(SaleOfFacilities.class);
            Root<SaleOfFacilities> saleOfFacilitiesRoot = saleOfFacilitiesCriteriaQuery.from(SaleOfFacilities.class);
            saleOfFacilitiesRoot.fetch(SaleOfFacilities_.facility, JoinType.LEFT);
            saleOfFacilitiesCriteriaQuery.select(saleOfFacilitiesRoot).distinct(true);

            return em.createQuery(saleOfFacilitiesCriteriaQuery).getResultList();
    }
}
