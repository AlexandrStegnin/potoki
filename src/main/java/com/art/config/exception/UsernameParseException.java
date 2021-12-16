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
public class UsernameParseException extends RuntimeException {

  String message;
  HttpStatus status;

  public static UsernameParseException build400Exception(String message) {
    return new UsernameParseException(message, HttpStatus.BAD_REQUEST);
  }

}
