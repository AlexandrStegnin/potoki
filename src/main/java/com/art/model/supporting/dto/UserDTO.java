package com.art.model.supporting.dto;

import com.art.model.AppRole;
import com.art.model.AppUser;
import com.art.model.UserProfile;
import com.art.model.supporting.enums.KinEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;

    String login;

    List<AppRoleDTO> roles;

    String kin;

    Long partnerId;

    String password;

    UserProfileDTO profile;

    public UserDTO(AppUser entity) {
        this.id = entity.getId();
        this.login = entity.getLogin();
        this.roles = convertRoles(entity.getRoles());
        this.kin = convertKin(entity.getKin());
        this.partnerId = entity.getPartnerId();
        this.profile = convertProfile(entity.getProfile());
    }

    private String convertKin(KinEnum kin) {
        if (kin != null) {
            return kin.getVal();
        }
        return null;
    }

    private List<AppRoleDTO> convertRoles(List<AppRole> roles) {
        return roles.stream()
                .map(AppRoleDTO::new)
                .collect(Collectors.toList());
    }

    private UserProfileDTO convertProfile(UserProfile profile) {
        return new UserProfileDTO(profile);
    }

}
