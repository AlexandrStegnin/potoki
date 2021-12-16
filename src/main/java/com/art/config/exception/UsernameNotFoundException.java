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
public class UsernameNotFoundException extends RuntimeException {

  String message;
  HttpStatus status;

  public static UsernameNotFoundException build404Exception(String message) {
    return new UsernameNotFoundException(message, HttpStatus.NOT_FOUND);
  }

}
