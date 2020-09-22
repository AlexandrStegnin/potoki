package com.art.service;

import com.art.model.Facility;
import com.art.model.Facility_;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.enums.OwnerType;
import com.art.repository.FacilityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FacilityService {

    private final FacilityRepository facilityRepository;

    private final AccountService accountService;

    public FacilityService(FacilityRepository facilityRepository, AccountService accountService) {
        this.facilityRepository = facilityRepository;
        this.accountService = accountService;
    }

//    @Cacheable(Constant.FACILITIES_CACHE_KEY)
    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    public Facility findById(Long id) {
        return facilityRepository.findOne(id);
    }

    public List<Facility> initializeFacilities() {
        List<Facility> facilitiesList = new ArrayList<>(0);
        Facility facility = new Facility();
        facility.setId(0L);
        facility.setName("Выберите объект");
        facilitiesList.add(facility);
        facilitiesList.addAll(findAll());
        return facilitiesList;
    }

    public List<Facility> initializeFacilitiesForMultiple() {
        return findAll();
    }

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public Facility findByName(String name) {
        return facilityRepository.findByName(name);
    }

//    @CacheEvict(Constant.FACILITIES_CACHE_KEY)
    public void deleteById(Long id) {
        facilityRepository.delete(id);
        accountService.deleteByOwnerId(id, OwnerType.FACILITY);
    }

//    @CachePut(value = Constant.FACILITIES_CACHE_KEY, key = "#facility.id")
    public void update(Facility facility) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<Facility> update = criteriaBuilder.createCriteriaUpdate(Facility.class);
        Root<Facility> facilityRoot = update.from(Facility.class);
        update.set(Facility_.name, facility.getName());
        update.set(Facility_.city, facility.getCity());
        update.set(Facility_.fullName, facility.getFullName());
        update.where(criteriaBuilder.equal(facilityRoot.get(Facility_.id), facility.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    /**
     * Создать объект и счёт к нему
     *
     * @param facility объект
     * @return ответ об успешности операции
     */
//    @Cacheable(Constant.FACILITIES_CACHE_KEY)
    public ApiResponse create(Facility facility) {
        ApiResponse apiResponse = accountService.checkAccountNumber(facility);
        if (apiResponse != null) {
            return apiResponse;
        }
        facilityRepository.saveAndFlush(facility);
        accountService.createAccount(facility);
        apiResponse = new ApiResponse("Объект " + facility.getName() + " успешно добавлен.", HttpStatus.OK.value());
        return apiResponse;
    }

}
