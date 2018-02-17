package com.art.service;

import com.art.model.FacilitiesBuySales;
import com.art.model.UnderFacilities;
import com.art.repository.FacilitiesBuySalesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class FacilitiesBuySalesService {

    @Resource(name = "facilitiesBuySalesRepository")
    private FacilitiesBuySalesRepository facilitiesBuySalesRepository;


    public List<FacilitiesBuySales> findAll() {
        return facilitiesBuySalesRepository.findAll();
    }

    public FacilitiesBuySales findById(BigInteger id) {
        return facilitiesBuySalesRepository.findOne(id);
    }

    public FacilitiesBuySales update(FacilitiesBuySales facilitiesBuySales) {
        return facilitiesBuySalesRepository.saveAndFlush(facilitiesBuySales);
    }

    public void deleteById(BigInteger id) {
        facilitiesBuySalesRepository.delete(id);
    }

    public void deleteByUnderFacility(UnderFacilities underFacilities) {
        facilitiesBuySalesRepository.deleteByUnderFacility(underFacilities);
    }

    public void create(FacilitiesBuySales facilitiesBuySales) {
        facilitiesBuySalesRepository.saveAndFlush(facilitiesBuySales);
    }
}
