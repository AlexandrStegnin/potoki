package com.art.model.supporting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String message;

    private int status;

    public ApiResponse(String message) {
        this.message = message;
        this.status = HttpStatus.OK.value();
    }

}
