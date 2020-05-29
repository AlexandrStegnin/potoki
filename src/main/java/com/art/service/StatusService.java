package com.art.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * @author Alexandr Stegnin
 */

@Service
public class StatusService {

    private final SimpMessageSendingOperations messageTemplate;

    public StatusService(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public void sendStatus(String status) {
        messageTemplate.convertAndSend("/progress/status", status);
    }

}
