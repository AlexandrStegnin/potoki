package com.art.model.supporting.dto;

import com.art.model.UserProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileDTO {

    Long id;

    String lastName;

    String firstName;

    String patronymic;

    String email;

    public UserProfileDTO(UserProfile profile) {
        this.id = profile.getId();
        this.lastName = profile.getLastName();
        this.firstName = profile.getFirstName();
        this.patronymic = profile.getPatronymic();
        this.email = profile.getEmail();
    }

}
