package com.art.service;

import com.art.model.UnderFacility;
import com.art.repository.UnderFacilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UnderFacilityService {

    private final UnderFacilityRepository underFacilityRepository;

    public UnderFacilityService(UnderFacilityRepository underFacilityRepository) {
        this.underFacilityRepository = underFacilityRepository;
    }

    public List<UnderFacility> findAll() {
        return underFacilityRepository.findAll();
    }

    public UnderFacility findByName(String name) {
        return underFacilityRepository.findByName(name);
    }

    public UnderFacility findById(Long id) {
        return underFacilityRepository.findOne(id);
    }

    public void deleteById(Long id) {
        underFacilityRepository.delete(id);
    }

    public void create(UnderFacility underFacility) {
        underFacilityRepository.saveAndFlush(underFacility);
    }

    public List<UnderFacility> findByFacilityId(Long id) {
        return underFacilityRepository.findByFacilityId(id);
    }

    public List<UnderFacility> initializeUnderFacilities() {
        List<UnderFacility> underFacilityList = new ArrayList<>(0);
        UnderFacility underFacility = new UnderFacility();
        underFacility.setId(0L);
        underFacility.setName("Выберите подобъект");
        underFacilityList.add(underFacility);
        underFacilityList.addAll(underFacilityRepository.findAll());
        return underFacilityList;
    }

    public List<UnderFacility> initializeUnderFacilitiesList() {
        return underFacilityRepository.findAll();
    }

    public void update(UnderFacility underFacility) {
        underFacilityRepository.save(underFacility);
    }
}
