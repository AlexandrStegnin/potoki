package com.art.model.supporting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String message;

    private int status;

}
