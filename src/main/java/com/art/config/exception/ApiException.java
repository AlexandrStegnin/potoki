package com.art.config.exception;

import lombok.Getter;

/**
 * @author Alexandr Stegnin
 */

@Getter
public class ApiException extends RuntimeException {

    private final String message;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

}
