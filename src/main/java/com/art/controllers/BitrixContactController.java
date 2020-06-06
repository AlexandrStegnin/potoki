package com.art.controllers;

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
    //TODO доставать ключ из базы
    private static String BITRIX_API_KEY;

    @Value("${bitrix.api.key}")
    public void setBitrixApiKey(String value) {
        BITRIX_API_KEY = value;
    }

    @PostMapping("/bitrix/merge")
    public String mergeContacts() {
        final String[] message = {""};
        RestTemplate restTemplate = new RestTemplate();
        message[0] = restTemplate
                .getForObject("http://api.ddkolesnik.com/v1/" + BITRIX_API_KEY + "/bitrix/merge", String.class);
        if (message[0].equalsIgnoreCase("\"OK\"")) {
            message[0] = "Синхронизация контактов завершена";
        }
        return message[0];
    }

}
