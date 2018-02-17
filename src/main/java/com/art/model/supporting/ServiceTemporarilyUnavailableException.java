package com.art.model.supporting;

public class ServiceTemporarilyUnavailableException extends RuntimeException {
    public ServiceTemporarilyUnavailableException(String message) {
        super(message);
    }
}
