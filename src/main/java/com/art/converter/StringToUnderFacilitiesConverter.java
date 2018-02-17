package com.art.converter;

import com.art.model.UnderFacilities;
import com.art.service.UnderFacilitiesService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;

@Component
public class StringToUnderFacilitiesConverter implements Converter<String, UnderFacilities> {

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    public StringToUnderFacilitiesConverter(UnderFacilitiesService underFacilitiesService) {
        this.underFacilitiesService = underFacilitiesService;
    }

    public UnderFacilities convert(String id) {
        UnderFacilities underFacilities;
        try {
            BigInteger IntId = new BigInteger(id);
            underFacilities = underFacilitiesService.findById(IntId);
        } catch (Exception ex) {
            underFacilities = underFacilitiesService.findByUnderFacility(id);
        }
        return underFacilities;
    }
}
