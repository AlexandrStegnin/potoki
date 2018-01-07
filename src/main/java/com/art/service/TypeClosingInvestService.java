package com.art.service;

import com.art.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Repository
public class TypeClosingInvestService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager entityManager;

    public List<TypeClosingInvest> findAllWithCriteriaApi (){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<TypeClosingInvest> typeClosingInvestCriteriaQuery = cb.createQuery(TypeClosingInvest.class);
        Root<TypeClosingInvest> typeClosingInvestRoot = typeClosingInvestCriteriaQuery.from(TypeClosingInvest.class);

        typeClosingInvestCriteriaQuery.select(typeClosingInvestRoot);
        return entityManager.createQuery(typeClosingInvestCriteriaQuery).getResultList();
    }

    public TypeClosingInvest findById(BigInteger id){
        return this.entityManager.find(TypeClosingInvest.class, id);
    }

    public TypeClosingInvest findByTypeClosingInvest(String typeClosingInvest){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TypeClosingInvest> typeClosingInvestCriteriaQuery = cb.createQuery(TypeClosingInvest.class);
        Root<TypeClosingInvest> typeClosingInvestRoot = typeClosingInvestCriteriaQuery.from(TypeClosingInvest.class);
        typeClosingInvestCriteriaQuery.select(typeClosingInvestRoot);
        typeClosingInvestCriteriaQuery.where(cb.equal(typeClosingInvestRoot.get(TypeClosingInvest_.typeClosingInvest), typeClosingInvest));

        return  entityManager.createQuery(typeClosingInvestCriteriaQuery).getSingleResult();
    }

    public void deleteById(BigInteger id){
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<TypeClosingInvest> delete = cb.createCriteriaDelete(TypeClosingInvest.class);
        Root<TypeClosingInvest> typeClosingInvestRoot = delete.from(TypeClosingInvest.class);
        delete.where(cb.equal(typeClosingInvestRoot.get(TypeClosingInvest_.id), id));
        this.entityManager.createQuery(delete).executeUpdate();
    }

    public void update(TypeClosingInvest typeClosingInvest){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaUpdate<TypeClosingInvest> update = criteriaBuilder.createCriteriaUpdate(TypeClosingInvest.class);
        Root<TypeClosingInvest> typeClosingInvestRoot = update.from(TypeClosingInvest.class);
        update.set(TypeClosingInvest_.typeClosingInvest, typeClosingInvest.getTypeClosingInvest());
        update.where(criteriaBuilder.equal(typeClosingInvestRoot.get(TypeClosingInvest_.id), typeClosingInvest.getId()));
        this.entityManager.createQuery(update).executeUpdate();
    }

    public void create(TypeClosingInvest typeClosingInvest){
        this.entityManager.persist(typeClosingInvest);
    }

    public List<TypeClosingInvest> init(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TypeClosingInvest> typeClosingInvestCriteriaQuery = cb.createQuery(TypeClosingInvest.class);
        Root<TypeClosingInvest> typeClosingInvestRoot = typeClosingInvestCriteriaQuery.from(TypeClosingInvest.class);
        typeClosingInvestCriteriaQuery.select(typeClosingInvestRoot);
        typeClosingInvestCriteriaQuery.where(cb.notEqual(typeClosingInvestRoot.get(TypeClosingInvest_.typeClosingInvest), "Реинвестирование"));
        TypeClosingInvest typeClosingInvest = new TypeClosingInvest();
        typeClosingInvest.setId(new BigInteger("0"));
        typeClosingInvest.setTypeClosingInvest("Выберите вид закрытия");
        List<TypeClosingInvest> typeClosingInvestList = new ArrayList<>(0);
        typeClosingInvestList.add(typeClosingInvest);
        typeClosingInvestList.addAll(entityManager.createQuery(typeClosingInvestCriteriaQuery).getResultList());
        return typeClosingInvestList;
    }
}
