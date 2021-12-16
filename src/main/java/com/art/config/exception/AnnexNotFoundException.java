package com.art.config.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AnnexNotFoundException extends RuntimeException {

  String message;
  HttpStatus status;

  public static AnnexNotFoundException build404Exception(String message) {
    return new AnnexNotFoundException(message, HttpStatus.NOT_FOUND);
  }

}
