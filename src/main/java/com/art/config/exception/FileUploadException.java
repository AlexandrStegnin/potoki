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
public class FileUploadException extends RuntimeException {

  String message;
  HttpStatus status;

  public FileUploadException(String message) {
    this.message = message;
    this.status = HttpStatus.BAD_REQUEST;
  }

  public static FileUploadException build400Exception(String message) {
    return new FileUploadException(message, HttpStatus.BAD_REQUEST);
  }

}
