package com.art.service;

import com.art.model.ShareKind;
import com.art.model.ShareKind_;
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
public class ShareKindService {
    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public List<ShareKind> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<ShareKind> shareKindCriteriaQuery = cb.createQuery(ShareKind.class);
        Root<ShareKind> shareKindRoot = shareKindCriteriaQuery.from(ShareKind.class);
        shareKindCriteriaQuery.select(shareKindRoot);

        return em.createQuery(shareKindCriteriaQuery).getResultList();
    }

    public ShareKind findById(BigInteger id) {
        return this.em.find(ShareKind.class, id);
    }

    public ShareKind findByShareKind(String shareKind) {
        return this.em.find(ShareKind.class, shareKind);
    }

    public void deleteById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<ShareKind> delete = cb.createCriteriaDelete(ShareKind.class);
        Root<ShareKind> shareKindRoot = delete.from(ShareKind.class);
        delete.where(cb.equal(shareKindRoot.get(ShareKind_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public void update(ShareKind shareKind) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<ShareKind> update = criteriaBuilder.createCriteriaUpdate(ShareKind.class);
        Root<ShareKind> shareKindRoot = update.from(ShareKind.class);
        update.set(ShareKind_.shareKind, shareKind.getShareKind());
        update.where(criteriaBuilder.equal(shareKindRoot.get(ShareKind_.id), shareKind.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public void create(ShareKind shareKind) {
        this.em.persist(shareKind);
    }

    public List<ShareKind> init() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ShareKind> shareKindCriteriaQuery = cb.createQuery(ShareKind.class);
        Root<ShareKind> shareKindRoot = shareKindCriteriaQuery.from(ShareKind.class);
        shareKindCriteriaQuery.select(shareKindRoot);
        ShareKind shareKind = new ShareKind();
        shareKind.setId(new BigInteger("0"));
        shareKind.setShareKind("Выберите вид доли");
        List<ShareKind> shareKindList = new ArrayList<>(0);
        shareKindList.add(shareKind);
        shareKindList.addAll(em.createQuery(shareKindCriteriaQuery).getResultList());
        return shareKindList;
    }
}
