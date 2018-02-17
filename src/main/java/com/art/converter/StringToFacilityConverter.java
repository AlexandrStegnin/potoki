package com.art.converter;

import com.art.model.Facilities;
import com.art.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToFacilityConverter implements Converter<String, Facilities> {

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Autowired
    public StringToFacilityConverter(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    public Facilities convert(String id) {
        Facilities facilities;
        try {
            BigInteger IntId = new BigInteger(id);
            facilities = facilityService.findById(IntId);
        } catch (Exception ex) {
            facilities = facilityService.findByFacility(id);
        }

        return facilities;
    }
}
