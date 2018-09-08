package com.art.service;

import com.art.model.AnnexToContracts;
import com.art.model.AnnexToContracts_;
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
public class AnnexToContractsService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;


    public AnnexToContracts findById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<AnnexToContracts> annexToContractsCriteriaQuery = cb.createQuery(AnnexToContracts.class);
        Root<AnnexToContracts> annexToContractsRoot = annexToContractsCriteriaQuery.from(AnnexToContracts.class);
        annexToContractsCriteriaQuery.select(annexToContractsRoot);
        annexToContractsCriteriaQuery.where(cb.equal(annexToContractsRoot.get(AnnexToContracts_.id), id));
        return em.createQuery(annexToContractsCriteriaQuery).getSingleResult();
    }

    public AnnexToContracts findByAnnex(String annex) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<AnnexToContracts> annexToContractsCriteriaQuery = cb.createQuery(AnnexToContracts.class);
        Root<AnnexToContracts> annexToContractsRoot = annexToContractsCriteriaQuery.from(AnnexToContracts.class);
        annexToContractsCriteriaQuery.select(annexToContractsRoot);
        annexToContractsCriteriaQuery.where(cb.equal(annexToContractsRoot.get(AnnexToContracts_.annexName), annex));

        return this.em.createQuery(annexToContractsCriteriaQuery).getSingleResult();
    }

    public void deleteById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<AnnexToContracts> delete = cb.createCriteriaDelete(AnnexToContracts.class);
        Root<AnnexToContracts> annexToContractsRoot = delete.from(AnnexToContracts.class);
        delete.where(cb.equal(annexToContractsRoot.get(AnnexToContracts_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public void update(AnnexToContracts annexToContracts) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaUpdate<AnnexToContracts> update = cb.createCriteriaUpdate(AnnexToContracts.class);
        Root<AnnexToContracts> annexToContractsRoot = update.from(AnnexToContracts.class);
        update.set(AnnexToContracts_.annexName, annexToContracts.getAnnexName());
        update.where(cb.equal(annexToContractsRoot.get(AnnexToContracts_.id), annexToContracts.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public AnnexToContracts create(AnnexToContracts annexToContracts) {
        this.em.persist(annexToContracts);
        this.em.flush();
        return annexToContracts;
    }

    public List<AnnexToContracts> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AnnexToContracts> annexToContractsCriteriaQuery = cb.createQuery(AnnexToContracts.class);
        Root<AnnexToContracts> annexToContractsRoot = annexToContractsCriteriaQuery.from(AnnexToContracts.class);
        annexToContractsCriteriaQuery.select(annexToContractsRoot);
        return em.createQuery(annexToContractsCriteriaQuery).getResultList();
    }

}
