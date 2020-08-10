package com.art.service;

import com.art.config.application.Constant;
import com.art.model.Account;
import com.art.model.Facility;
import com.art.model.UnderFacility;
import com.art.model.supporting.enums.OwnerType;
import com.art.repository.UnderFacilityRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UnderFacilityService {

    private final UnderFacilityRepository underFacilityRepository;

    private final AccountService accountService;

    public UnderFacilityService(UnderFacilityRepository underFacilityRepository, AccountService accountService) {
        this.underFacilityRepository = underFacilityRepository;
        this.accountService = accountService;
    }

    @Cacheable(Constant.UNDER_FACILITIES_CACHE_KEY)
    public List<UnderFacility> findAll() {
        return underFacilityRepository.findAll();
    }

    @Cacheable(Constant.UNDER_FACILITIES_CACHE_KEY)
    public UnderFacility findByName(String name) {
        return underFacilityRepository.findByName(name);
    }

    @Cacheable(Constant.UNDER_FACILITIES_CACHE_KEY)
    public UnderFacility findById(Long id) {
        return underFacilityRepository.findOne(id);
    }

    @CacheEvict(Constant.UNDER_FACILITIES_CACHE_KEY)
    public void deleteById(Long id) {
        underFacilityRepository.delete(id);
        accountService.deleteByOwnerId(id, OwnerType.UNDER_FACILITY);
    }

    @CachePut(Constant.UNDER_FACILITIES_CACHE_KEY)
    public void create(UnderFacility underFacility) {
        underFacilityRepository.saveAndFlush(underFacility);
        Facility facility = underFacility.getFacility();
        Account account = accountService.findByOwnerId(facility.getId(), OwnerType.FACILITY);
        int countUnderFacilities = underFacilityRepository.countByFacilityId(facility.getId());
        accountService.createAccount(underFacility.getId(), account, countUnderFacilities);
    }

    @Cacheable(Constant.UNDER_FACILITIES_CACHE_KEY)
    public List<UnderFacility> findByFacilityId(Long id) {
        return underFacilityRepository.findByFacilityId(id);
    }

    public List<UnderFacility> initializeUnderFacilities() {
        List<UnderFacility> underFacilityList = new ArrayList<>(0);
        UnderFacility underFacility = new UnderFacility();
        underFacility.setId(0L);
        underFacility.setName("Выберите подобъект");
        underFacilityList.add(underFacility);
        underFacilityList.addAll(findAll());
        return underFacilityList;
    }

    public List<UnderFacility> initializeUnderFacilitiesList() {
        return findAll();
    }

    @CachePut(value = Constant.UNDER_FACILITIES_CACHE_KEY, key = "#underFacility.id")
    public void update(UnderFacility underFacility) {
        underFacilityRepository.save(underFacility);
    }
}
