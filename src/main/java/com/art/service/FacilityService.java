package com.art.service;

import com.art.model.Facility;
import com.art.model.Facility_;
import com.art.model.supporting.UserFacilities;
import com.art.repository.FacilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    public Facility findById(Long id) {
        return facilityRepository.findById(id);
    }

    public List<UserFacilities> getInvestorsFacility(Long rentorInvestorId) {
        return facilityRepository.getInvestorsFacility(rentorInvestorId);
    }

    public List<Facility> initializeFacilities() {
        List<Facility> facilitiesList = new ArrayList<>(0);
        Facility facility = new Facility();
        facility.setId(Long.valueOf("0"));
        facility.setName("Выберите объект");
        facilitiesList.add(facility);
        facilitiesList.addAll(facilityRepository.findAll());
        return facilitiesList;
    }

    public List<Facility> initializeFacilitiesForMultiple() {
        return facilityRepository.findAll();
    }

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public List<Facility> findAllWithUnderFacilities() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facility> facilityesCriteriaQuery = cb.createQuery(Facility.class);
        Root<Facility> facilityesRoot = facilityesCriteriaQuery.from(Facility.class);
        facilityesRoot.fetch(Facility_.underFacilities, JoinType.LEFT);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        return em.createQuery(facilityesCriteriaQuery).getResultList();
    }

    public Facility findByFacility(String facility) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facility> facilityesCriteriaQuery = cb.createQuery(Facility.class);
        Root<Facility> facilitiesRoot = facilityesCriteriaQuery.from(Facility.class);
        facilityesCriteriaQuery.select(facilitiesRoot);
        facilityesCriteriaQuery.where(cb.equal(facilitiesRoot.get(Facility_.name), facility));

        return this.em.createQuery(facilityesCriteriaQuery).getSingleResult();
    }


    public void deleteById(Long id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<Facility> delete = cb.createCriteriaDelete(Facility.class);
        Root<Facility> facilityRoot = delete.from(Facility.class);
        delete.where(cb.equal(facilityRoot.get(Facility_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public void update(Facility facility) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<Facility> update = criteriaBuilder.createCriteriaUpdate(Facility.class);
        Root<Facility> facilityesRoot = update.from(Facility.class);
        update.set(Facility_.name, facility.getName());
        update.set(Facility_.city, facility.getCity());
        update.set(Facility_.fullName, facility.getFullName());
        update.where(criteriaBuilder.equal(facilityesRoot.get(Facility_.id), facility.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public void create(Facility facility) {
        this.em.merge(facility);
    }

    public Facility findByIdWithUnderFacilitiesAndRooms(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facility> facilityesCriteriaQuery = cb.createQuery(Facility.class);
        Root<Facility> facilityesRoot = facilityesCriteriaQuery.from(Facility.class);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        facilityesCriteriaQuery.where(cb.equal(facilityesRoot.get(Facility_.id), id));

        Facility facility = em.createQuery(facilityesCriteriaQuery).getSingleResult();
        facility.getUnderFacilities().forEach(uf -> uf.getRooms().size());
        return facility;
    }

}
