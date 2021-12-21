package com.art.model.supporting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String message;

    private int status;

    private String error;

    public ApiResponse(String message) {
        this.message = message;
        this.status = HttpStatus.OK.value();
    }

    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

}
