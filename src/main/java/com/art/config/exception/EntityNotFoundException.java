package com.art.config.exception;

/**
 * @author Alexandr Stegnin
 */
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String message) {
    super(message);
  }

}
