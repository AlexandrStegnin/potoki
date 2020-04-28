package com.art.service;

import com.art.model.MailingGroups;
import com.art.model.MailingGroups_;
import com.art.repository.MailingGroupsRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class MailingGroupsService {

    @Resource(name = "mailingGroupsRepository")
    private MailingGroupsRepository mailingGroupsRepository;

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public MailingGroups create(MailingGroups mailingGroups) {
        return mailingGroupsRepository.saveAndFlush(mailingGroups);
    }

    public List<MailingGroups> findAll() {
        return mailingGroupsRepository.findAll();
    }

    public MailingGroups findById(BigInteger id) {
        return mailingGroupsRepository.findOne(id);
    }

    public MailingGroups update(MailingGroups mailingGroups) {
        return mailingGroupsRepository.saveAndFlush(mailingGroups);
    }

    public void deleteById(BigInteger id) {
        mailingGroupsRepository.delete(id);
    }

    public List<MailingGroups> findAllWithUsers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<MailingGroups> mailingGroupsCriteriaQuery = cb.createQuery(MailingGroups.class);
        Root<MailingGroups> mailingGroupsRoot = mailingGroupsCriteriaQuery.from(MailingGroups.class);
        mailingGroupsRoot.fetch(MailingGroups_.users, JoinType.LEFT);

        mailingGroupsCriteriaQuery.select(mailingGroupsRoot).distinct(true);

        return em.createQuery(mailingGroupsCriteriaQuery).getResultList();
    }

    public MailingGroups findByIdWithUsers(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<MailingGroups> mailingGroupsCriteriaQuery = cb.createQuery(MailingGroups.class);
        Root<MailingGroups> mailingGroupsRoot = mailingGroupsCriteriaQuery.from(MailingGroups.class);
        mailingGroupsRoot.fetch(MailingGroups_.users, JoinType.LEFT);

        mailingGroupsCriteriaQuery.select(mailingGroupsRoot).distinct(true);
        mailingGroupsCriteriaQuery.where(cb.equal(mailingGroupsRoot.get(MailingGroups_.id), id));
        return em.createQuery(mailingGroupsCriteriaQuery).getSingleResult();
    }

    public MailingGroups findByIdWithAllFields(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<MailingGroups> mailingGroupsCriteriaQuery = cb.createQuery(MailingGroups.class);
        Root<MailingGroups> mailingGroupsRoot = mailingGroupsCriteriaQuery.from(MailingGroups.class);
        mailingGroupsCriteriaQuery.select(mailingGroupsRoot);
        mailingGroupsCriteriaQuery.where(cb.equal(mailingGroupsRoot.get(MailingGroups_.id), id));

        MailingGroups group = em.createQuery(mailingGroupsCriteriaQuery).getSingleResult();
        group.getUsers().forEach(u -> {
            Hibernate.initialize(u.getFacilities());
            Hibernate.initialize(u.getUsersAnnexToContractsList());
            Hibernate.initialize(u.getEmails());
            Hibernate.initialize(u.getUserStuff());
        });

        return group;
    }

    public MailingGroups findByGroupWithUsers(String group) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MailingGroups> mailingGroupsCriteriaQuery = cb.createQuery(MailingGroups.class);
        Root<MailingGroups> mailingGroupsRoot = mailingGroupsCriteriaQuery.from(MailingGroups.class);
        mailingGroupsRoot.fetch(MailingGroups_.users, JoinType.LEFT);
        mailingGroupsCriteriaQuery.select(mailingGroupsRoot).distinct(true);
        mailingGroupsCriteriaQuery.where(cb.like(cb.lower(mailingGroupsRoot.get(MailingGroups_.mailingGroup)),
                "%" + group.toLowerCase()));
        MailingGroups mailingGroups = new MailingGroups();
        try {
            mailingGroups = em.createQuery(mailingGroupsCriteriaQuery).getSingleResult();
        } catch (NoResultException ex) {
            ex.getLocalizedMessage();
        }

        return mailingGroups;
    }

    public List<MailingGroups> findByIdIn(List<BigInteger> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MailingGroups> mailingGroupsCriteriaQuery = cb.createQuery(MailingGroups.class);
        Root<MailingGroups> mailingGroupsRoot = mailingGroupsCriteriaQuery.from(MailingGroups.class);
        mailingGroupsRoot.fetch(MailingGroups_.users, JoinType.LEFT);
        mailingGroupsCriteriaQuery.select(mailingGroupsRoot);
        mailingGroupsCriteriaQuery.where(mailingGroupsRoot.get(MailingGroups_.id).in(idList));
        return em.createQuery(mailingGroupsCriteriaQuery).getResultList();
    }

}
