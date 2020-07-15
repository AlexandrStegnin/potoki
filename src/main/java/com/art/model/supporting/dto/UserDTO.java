package com.art.model.supporting.dto;

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

    String login;

    List<AppRoleDTO> roles;

    String kin;

    Long partnerId;

    String password;

    UserProfileDTO profile;

}
