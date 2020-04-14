package com.art.converter;

import com.art.model.AppToken;
import com.art.service.AppTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Alexandr Stegnin
 */

public class StringToTokenConverter implements Converter<String, AppToken> {

    private final AppTokenService appTokenService;

    @Autowired
    public StringToTokenConverter(AppTokenService appTokenService) {
        this.appTokenService = appTokenService;
    }

    public AppToken convert(String id) {
        AppToken appToken;
        try {
            Long intId = new Long(id);
            appToken = appTokenService.findById(intId);
        } catch (Exception ex) {
            appToken = appTokenService.findByAppName(id);
        }

        return appToken;
    }

}
