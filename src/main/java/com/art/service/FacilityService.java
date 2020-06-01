package com.art.service;

import com.art.model.*;
import com.art.model.supporting.UserFacilities;
import com.art.repository.FacilityRepository;
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
import java.util.Set;

@Service
@Transactional
@Repository
public class FacilityService {

    //@Autowired
    @Resource(name = "facilityRepository")
    private FacilityRepository facilityRepository;

    public List<Facilities> findAll() {
        return facilityRepository.findAll();
    }

    public Facilities findById(BigInteger id) {
        return facilityRepository.findById(id);
    }

    public List<Facilities> findByIdNot(BigInteger id) {
        return facilityRepository.findByIdNot(id);
    }

    public List<UserFacilities> getInvestorsFacility(BigInteger rentorInvestorId) {
        return facilityRepository.getInvestorsFacility(rentorInvestorId);
    }

    public Facilities findByManager(Users manager) {
        return facilityRepository.findByManager(manager);
    }

    public List<Facilities> initializeFacilities() {
        List<Facilities> facilitiesList = new ArrayList<>(0);
        Facilities facility = new Facilities();
        facility.setId(new BigInteger("0"));
        facility.setFacility("Выберите объект");
        facilitiesList.add(facility);
        facilitiesList.addAll(facilityRepository.findAll());
        return facilitiesList;
    }

    public List<Facilities> initializeFacilitiesForMultiple() {
        return facilityRepository.findAll();
    }

    public List<Facilities> findByIdIn(List<BigInteger> idList) {
        return facilityRepository.findByIdIn(idList);
    }

    public void updateList(List<Facilities> facilitiesList) {
        facilityRepository.save(facilitiesList);
    }

    public Facilities findByInvestors(Set<Users> usersSet) {
        return facilityRepository.findByInvestors(usersSet);
    }

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public List<Facilities> findAllWithManagers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityServiceRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityServiceRoot.fetch(Facilities_.manager, JoinType.LEFT);
        facilityesCriteriaQuery.select(facilityServiceRoot).distinct(true);

        return em.createQuery(facilityesCriteriaQuery).getResultList();
    }


    public Facilities findByIdWithManagers(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesRoot.fetch(Facilities_.manager, JoinType.INNER);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        facilityesCriteriaQuery.where(cb.equal(facilityesRoot.get(Facilities_.id), id));
        return em.createQuery(facilityesCriteriaQuery).getSingleResult();
    }

    public Facilities findByIdWithUnderFacilities(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesRoot.fetch(Facilities_.underFacilities, JoinType.LEFT);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        facilityesCriteriaQuery.where(cb.equal(facilityesRoot.get(Facilities_.id), id));
        return em.createQuery(facilityesCriteriaQuery).getSingleResult();
    }

    public Facilities findByIdWithRentorInvestors(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesRoot.fetch(Facilities_.investors, JoinType.LEFT)
                .fetch(Users_.userStuff, JoinType.LEFT);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        facilityesCriteriaQuery.where(cb.equal(facilityesRoot.get(Facilities_.id), id));
        return em.createQuery(facilityesCriteriaQuery).getSingleResult();
    }

    public Facilities findByIdWithInvestors(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilitiesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilitiesRoot = facilitiesCriteriaQuery.from(Facilities.class);
        facilitiesRoot.fetch(Facilities_.investors, JoinType.LEFT);
        facilitiesRoot.fetch(Facilities_.underFacilities, JoinType.LEFT);
        facilitiesCriteriaQuery.select(facilitiesRoot).distinct(true);
        facilitiesCriteriaQuery.where(cb.equal(facilitiesRoot.get(Facilities_.id), id));
        return em.createQuery(facilitiesCriteriaQuery).getSingleResult();
    }

    public Facilities findByIdWithRentorsInvestorsManagers(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesRoot.fetch(Facilities_.investors, JoinType.LEFT);
        facilityesRoot.fetch(Facilities_.manager, JoinType.LEFT);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        facilityesCriteriaQuery.where(cb.equal(facilityesRoot.get(Facilities_.id), id));
        return em.createQuery(facilityesCriteriaQuery).getSingleResult();
    }

    public List<Facilities> findAllWithRentorsInvestorsManagers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesRoot.fetch(Facilities_.investors, JoinType.LEFT);
        facilityesRoot.fetch(Facilities_.manager, JoinType.LEFT);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        return em.createQuery(facilityesCriteriaQuery).getResultList();
    }

    public List<Facilities> findAllWithUnderFacilitiesRentorsInvestorsManagers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesRoot.fetch(Facilities_.investors, JoinType.LEFT)
                .fetch(Users_.userStuff, JoinType.LEFT);
        facilityesRoot.fetch(Facilities_.manager, JoinType.LEFT)
                .fetch(Users_.userStuff, JoinType.LEFT);
        facilityesRoot.fetch(Facilities_.underFacilities, JoinType.LEFT);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        return em.createQuery(facilityesCriteriaQuery).getResultList();
    }

    public Facilities findByFacility(String facility) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilitiesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesCriteriaQuery.select(facilitiesRoot);
        facilityesCriteriaQuery.where(cb.equal(facilitiesRoot.get(Facilities_.facility), facility));

        return this.em.createQuery(facilityesCriteriaQuery).getSingleResult();
    }


    public void deleteById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<Facilities> delete = cb.createCriteriaDelete(Facilities.class);
        Root<Facilities> facilityesRoot = delete.from(Facilities.class);
        delete.where(cb.equal(facilityesRoot.get(Facilities_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public void update(Facilities facility) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<Facilities> update = criteriaBuilder.createCriteriaUpdate(Facilities.class);
        Root<Facilities> facilityesRoot = update.from(Facilities.class);
        update.set(Facilities_.facility, facility.getFacility());
        update.set(Facilities_.city, facility.getCity());
        update.set(Facilities_.address, facility.getAddress());
        update.set(Facilities_.manager, facility.getManager());
        update.set(Facilities_.fullName, facility.getFullName());
        update.where(criteriaBuilder.equal(facilityesRoot.get(Facilities_.id), facility.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public void merge(Facilities facility) {
        this.em.merge(facility);
    }

    public void create(Facilities facility) {
        this.em.merge(facility);
    }

    public Facilities findByIdWithUnderFacilitiesAndRooms(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesCriteriaQuery.select(facilityesRoot).distinct(true);
        facilityesCriteriaQuery.where(cb.equal(facilityesRoot.get(Facilities_.id), id));

        Facilities facility = em.createQuery(facilityesCriteriaQuery).getSingleResult();
        facility.getUnderFacilities().forEach(uf -> uf.getRooms().size());
        return facility;
    }

    public List<Facilities> init() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facilities> facilityesCriteriaQuery = cb.createQuery(Facilities.class);
        Root<Facilities> facilityesRoot = facilityesCriteriaQuery.from(Facilities.class);
        facilityesCriteriaQuery.select(facilityesRoot);
        Facilities facility = new Facilities();
        facility.setId(new BigInteger("0"));
        facility.setFacility("Выберите объект");
        List<Facilities> facilitiesList = new ArrayList<>(0);
        facilitiesList.add(facility);
        facilitiesList.addAll(em.createQuery(facilityesCriteriaQuery).getResultList());
        return facilitiesList;
    }

}
