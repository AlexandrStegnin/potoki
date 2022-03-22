package com.art.model.supporting;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {

  String message;
  int status;
  String error;

  public ApiResponse(String message) {
    this.message = message;
    this.status = HttpStatus.OK.value();
  }

  public ApiResponse(String message, int status) {
    this.message = message;
    this.status = status;
  }

  public static ApiResponse build422Response(String message) {
    return new ApiResponse(message, HttpStatus.PRECONDITION_FAILED.value());
  }

  public static ApiResponse build200Response(String message) {
    return new ApiResponse(message, HttpStatus.OK.value());
  }

}
