package com.art.service;

import com.art.model.*;
import com.art.repository.UnderFacilitiesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Repository
public class UnderFacilitiesService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    @Resource(name = "underFacilitiesRepository")
    private UnderFacilitiesRepository underFacilitiesRepository;

    public List<UnderFacilities> findAll() {
        return underFacilitiesRepository.findAll();
    }

    public UnderFacilities findByUnderFacility(String underFacility) {
        return underFacilitiesRepository.findByUnderFacility(underFacility);
    }

    public UnderFacilities findById(BigInteger id) {
        return underFacilitiesRepository.findOne(id);
    }

    public void deleteById(BigInteger id) {
        underFacilitiesRepository.delete(id);
    }

    public void create(UnderFacilities underFacilities) {
        underFacilitiesRepository.saveAndFlush(underFacilities);
    }

    public List<UnderFacilities> findByFacilityId(BigInteger id) {
        return underFacilitiesRepository.findByFacilityId(id);
    }

    public void updateList(List<UnderFacilities> underFacilitiesList) {
        underFacilitiesRepository.save(underFacilitiesList);
    }

    public List<UnderFacilities> initializeUnderFacilities() {
        List<UnderFacilities> underFacilitiesList = new ArrayList<>(0);
        UnderFacilities underFacilities = new UnderFacilities();
        underFacilities.setId(new BigInteger("0"));
        underFacilities.setUnderFacility("Выберите подобъект");
        underFacilitiesList.add(underFacilities);
        underFacilitiesList.addAll(underFacilitiesRepository.findAll());
        return underFacilitiesList;
    }

    public List<UnderFacilities> findAllWithCriteriaApi() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UnderFacilities> facilityesCriteriaQuery = cb.createQuery(UnderFacilities.class);
        Root<UnderFacilities> underFacilitiesRoot = facilityesCriteriaQuery.from(UnderFacilities.class);
        underFacilitiesRoot.fetch(UnderFacilities_.facility, JoinType.LEFT);
        underFacilitiesRoot.fetch(UnderFacilities_.rooms, JoinType.LEFT);
        facilityesCriteriaQuery.select(underFacilitiesRoot).distinct(true);

        return em.createQuery(facilityesCriteriaQuery).getResultList();
    }

    public UnderFacilities findByIdWithCriteriaApi(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UnderFacilities> underFacilitiesCriteriaQuery = cb.createQuery(UnderFacilities.class);
        Root<UnderFacilities> underFacilitiesRoot = underFacilitiesCriteriaQuery.from(UnderFacilities.class);
        underFacilitiesRoot.fetch(UnderFacilities_.facility, JoinType.LEFT);
        underFacilitiesRoot.fetch(UnderFacilities_.rooms, JoinType.LEFT);
        underFacilitiesCriteriaQuery.select(underFacilitiesRoot).distinct(true);
        underFacilitiesCriteriaQuery.where(cb.equal(underFacilitiesRoot.get(UnderFacilities_.id), id));
        return em.createQuery(underFacilitiesCriteriaQuery).getSingleResult();
    }

    public void update(UnderFacilities underFacilities) {
        em.merge(underFacilities);
    }
}
