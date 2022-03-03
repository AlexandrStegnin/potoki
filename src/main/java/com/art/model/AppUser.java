package com.art.model;


import com.art.model.supporting.dto.AppRoleDTO;
import com.art.model.supporting.dto.UserDTO;
import com.art.model.supporting.enums.KinEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_user")
@ToString(of = {"id", "login"})
@EqualsAndHashCode(of = {"id", "login"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUser implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_generator")
  @SequenceGenerator(name = "app_user_generator", sequenceName = "app_user_id_seq")
  Long id;

  @Column(name = "login", unique = true, nullable = false, length = 30)
  String login;

  @JsonIgnore
  @Column(name = "password", nullable = false, length = 100)
  String password;

  @OneToOne
  @JoinColumn(name = "partner_id")
  AppUser partner;

  @Column(name = "confirmed")
  boolean confirmed;

  @OneToOne
  @JoinColumn(name = "role_id")
  AppRole role;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "kin")
  KinEnum kin;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  UserProfile profile;

  @Column(name = "phone")
  String phone;

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
    this.partner = makePartner(userDTO.getPartnerId());
    this.password = userDTO.getPassword();
    this.phone = userDTO.getPhone();
  }

  AppRole convertRole(AppRoleDTO dto) {
    if (Objects.isNull(dto)) {
      return null;
    }
    return new AppRole(dto);
  }

  AppUser makePartner(Long partnerId) {
    if (Objects.nonNull(partnerId) && partnerId != 0) {
      AppUser partner = new AppUser();
      partner.setId(partnerId);
      return partner;
    }
    return null;
  }

}
