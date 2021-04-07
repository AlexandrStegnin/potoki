package com.art.model;


import com.art.model.supporting.dto.AppRoleDTO;
import com.art.model.supporting.dto.UserDTO;
import com.art.model.supporting.enums.KinEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "app_user")
@ToString(of = {"id", "login"})
@EqualsAndHashCode(of = {"id", "login"})
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login", unique = true, nullable = false, length = 30)
    private String login;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @OneToOne
    @JoinColumn(name = "partner_id")
    private AppUser partner;

    @Column(name = "confirmed")
    private boolean confirmed;

    @OneToOne
    @JoinColumn(name = "role_id")
    private AppRole role;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "kin")
    private KinEnum kin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    public AppUser() {
    }

    public AppUser(Long id, AppUser partner) {
        this.id = id;
        this.partner = partner;
    }

    public AppUser(UserDTO userDTO) {
        this.id = userDTO.getId() != null ? userDTO.getId() : null;
        this.profile = new UserProfile(userDTO.getProfile());
        this.login = userDTO.getLogin();
        this.role = convertRole(userDTO.getRole());
        this.kin = userDTO.getKin() == null ? null : KinEnum.fromValue(userDTO.getKin());
        this.partner = new AppUser();
        this.partner.id = userDTO.getPartnerId();
        this.password = userDTO.getPassword();
    }

    private AppRole convertRole(AppRoleDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return new AppRole(dto);
    }

}
