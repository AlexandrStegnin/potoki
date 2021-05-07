package com.art.model.supporting.dto;

import com.art.model.AppToken;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenDTO {

    Long id;

    String appName;

    String token;

    public TokenDTO(AppToken token) {
        this.id = token.getId();
        this.appName = token.getAppName();
        this.token = token.getToken();
    }

}
