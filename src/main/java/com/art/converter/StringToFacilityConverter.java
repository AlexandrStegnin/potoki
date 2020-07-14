package com.art.converter;

import com.art.model.Facility;
import com.art.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToFacilityConverter implements Converter<String, Facility> {

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Autowired
    public StringToFacilityConverter(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    public Facility convert(String id) {
        Facility facility;
        try {
            BigInteger IntId = new BigInteger(id);
            facility = facilityService.findById(IntId);
        } catch (Exception ex) {
            facility = facilityService.findByFacility(id);
        }

        return facility;
    }
}
