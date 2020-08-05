package com.art.converter;

import com.art.model.UnderFacility;
import com.art.service.UnderFacilityService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUnderFacilityConverter implements Converter<String, UnderFacility> {

    private final UnderFacilityService underFacilityService;

    public StringToUnderFacilityConverter(UnderFacilityService underFacilityService) {
        this.underFacilityService = underFacilityService;
    }

    public UnderFacility convert(String id) {
        UnderFacility underFacility;
        try {
            Long IntId = Long.valueOf(id);
            underFacility = underFacilityService.findById(IntId);
        } catch (Exception ex) {
            underFacility = underFacilityService.findByName(id);
        }
        return underFacility;
    }
}
