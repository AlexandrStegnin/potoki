package com.art.service;

import com.art.model.FacilitiesReserves;
import com.art.repository.FacilitiesReservesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class FacilitiesReservesService {

    @Resource(name = "facilitiesReservesRepository")
    private FacilitiesReservesRepository facilitiesReservesRepository;

    public List<FacilitiesReserves> createList(List<FacilitiesReserves> facilitiesReserves) {
        return facilitiesReservesRepository.save(facilitiesReserves);
    }

    public FacilitiesReserves create(FacilitiesReserves facilitiesReserves) {
        return facilitiesReservesRepository.saveAndFlush(facilitiesReserves);
    }

    public List<FacilitiesReserves> findAll() {
        return facilitiesReservesRepository.findAll();
    }

    public FacilitiesReserves findById(BigInteger id) {
        return facilitiesReservesRepository.findOne(id);
    }

    public FacilitiesReserves update(FacilitiesReserves facilitiesReserves) {
        return facilitiesReservesRepository.saveAndFlush(facilitiesReserves);
    }

    public void deleteById(BigInteger id) {
        facilitiesReservesRepository.delete(id);
    }

}
