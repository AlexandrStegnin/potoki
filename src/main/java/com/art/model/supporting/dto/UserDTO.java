package com.art.model.supporting.dto;

import com.art.model.UserProfile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;

    String lastName;

    String firstName;

    String middleName;

    String login;

    String email;

    List<RoleDTO> roles;

    String kin;

    Long partnerId;

    UserProfile profile;

}
