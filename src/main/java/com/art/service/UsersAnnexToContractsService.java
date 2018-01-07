package com.art.service;

import com.art.model.AnnexToContracts;
import com.art.model.UsersAnnexToContracts;
import com.art.model.UsersAnnexToContracts_;
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
public class UsersAnnexToContractsService {
    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public List<UsersAnnexToContracts> findAll(){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UsersAnnexToContracts> usersAnnexToContractsCriteriaQuery = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = usersAnnexToContractsCriteriaQuery.from(UsersAnnexToContracts.class);
        usersAnnexToContractsCriteriaQuery.select(usersAnnexToContractsRoot);

        return em.createQuery(usersAnnexToContractsCriteriaQuery).getResultList();
    }

    public UsersAnnexToContracts findById(BigInteger id){
        return this.em.find(UsersAnnexToContracts.class, id);
    }

    public List<UsersAnnexToContracts> findByUserIdAndAnnex(BigInteger userId, AnnexToContracts annex){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UsersAnnexToContracts> usersAnnexToContractsCriteriaQuery = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = usersAnnexToContractsCriteriaQuery.from(UsersAnnexToContracts.class);
        usersAnnexToContractsCriteriaQuery.select(usersAnnexToContractsRoot);
        usersAnnexToContractsCriteriaQuery.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.userId), userId),
                cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.annex), annex));

        return em.createQuery(usersAnnexToContractsCriteriaQuery).getResultList();

    }

    public List<UsersAnnexToContracts> findByUserId(BigInteger userId){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UsersAnnexToContracts> usersAnnexToContractsCriteriaQuery = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = usersAnnexToContractsCriteriaQuery.from(UsersAnnexToContracts.class);
        usersAnnexToContractsCriteriaQuery.select(usersAnnexToContractsRoot);
        usersAnnexToContractsCriteriaQuery.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.userId), userId));

        return em.createQuery(usersAnnexToContractsCriteriaQuery).getResultList();

    }

    public void deleteById(BigInteger id){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<UsersAnnexToContracts> delete = cb.createCriteriaDelete(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = delete.from(UsersAnnexToContracts.class);
        delete.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public void deleteByAnnex(AnnexToContracts annex){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<UsersAnnexToContracts> delete = cb.createCriteriaDelete(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = delete.from(UsersAnnexToContracts.class);
        delete.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.annex), annex));
        this.em.createQuery(delete).executeUpdate();
    }

    public void update(UsersAnnexToContracts usersAnnexToContracts){

        this.em.merge(usersAnnexToContracts);
        /*
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<UsersAnnexToContracts> update = criteriaBuilder.createCriteriaUpdate(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = update.from(UsersAnnexToContracts.class);
        update.set(UsersAnnexToContracts_.annexRead, usersAnnexToContracts.getAnnexRead());
        update.set(UsersAnnexToContracts_.dateRead, usersAnnexToContracts.getDateRead());
        update.where(criteriaBuilder.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.id), usersAnnexToContracts.getId()));
        this.em.createQuery(update).executeUpdate();
        */

    }

    public void create(UsersAnnexToContracts usersAnnexToContracts){
        this.em.persist(usersAnnexToContracts);
    }

}
