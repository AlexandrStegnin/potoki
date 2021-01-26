package com.art.service;

import com.art.model.Facility;
import com.art.model.Facility_;
import com.art.model.Money;
import com.art.model.UnderFacility;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.FacilityDTO;
import com.art.model.supporting.enums.OwnerType;
import com.art.repository.FacilityRepository;
import com.art.repository.MoneyRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@Transactional
public class FacilityService {

    private final FacilityRepository facilityRepository;

    private final AccountService accountService;

    private final MoneyRepository moneyRepository;

    private final UnderFacilityService underFacilityService;

    public FacilityService(FacilityRepository facilityRepository, AccountService accountService,
                           MoneyRepository moneyRepository, UnderFacilityService underFacilityService) {
        this.facilityRepository = facilityRepository;
        this.accountService = accountService;
        this.moneyRepository = moneyRepository;
        this.underFacilityService = underFacilityService;
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

    /**
     * Удалить объект на основании DTO
     *
     * @param dto DTO для удаления
     * @return ответ
     */
    public ApiResponse delete(FacilityDTO dto) {
        Facility facility = findById(dto.getId());
        if (facility == null) {
            throw new RuntimeException("Не найден объект для удаления");
        }
        List<Money> monies = moneyRepository.findByFacilityId(facility.getId());
        if (monies.size() > 0) {
            return new ApiResponse(String.format("В объект [%s] вложены деньги, необходимо перераспределить их", facility.getName()), HttpStatus.BAD_REQUEST.value());
        }
        try {
            List<UnderFacility> underFacilities = underFacilityService.findByFacilityId(facility.getId());
            underFacilities.forEach(underFacility -> underFacilityService.deleteById(underFacility.getId()));
            deleteById(facility.getId());
            return new ApiResponse("Объект " + facility.getName() + " успешно удалён.");
        } catch (Exception e) {
            log.error("Произошла ошибка: {}", e.getLocalizedMessage());
            return new ApiResponse("При удалении объекта " + facility.getName() + " произошла ошибка.", HttpStatus.BAD_REQUEST.value());
        }
    }

    /**
     * Обновить объект на основе DTO
     *
     * @param dto DTO объекта
     * @return ответ
     */
    public ApiResponse update(FacilityDTO dto) {
        Facility facility = new Facility(dto);
        facilityRepository.save(facility);
        return new ApiResponse("Объект успешно обновлён");
    }

}
