package com.art.controllers;

import com.art.config.application.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexandr Stegnin
 */

@Slf4j
@RestController
public class BitrixContactController {

    private static String BITRIX_UPDATE_URL;

    @Value("${bitrix.update.url}")
    public void setBitrixUpdateUrl(String value) {
        BITRIX_UPDATE_URL = value;
    }

    @PostMapping(path = Location.BITRIX_MERGE)
    public String mergeContacts() {
        String message;
        RestTemplate restTemplate = new RestTemplate();
        message = restTemplate
                .getForObject(BITRIX_UPDATE_URL, String.class);
        if (message.contains("Saved contacts list size:")) {
            message = "Синхронизация контактов завершена";
        }
        return message;
    }

}
