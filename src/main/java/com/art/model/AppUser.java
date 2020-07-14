package com.art.model;


import com.art.model.supporting.dto.RoleDTO;
import com.art.model.supporting.dto.UserDTO;
import com.art.model.supporting.enums.KinEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "app_user")
@ToString(of = {"id", "login"})
@EqualsAndHashCode(of = {"id", "login"})
public class AppUser implements Serializable {

    private Long id;

    private String login;

    private String password;

    private List<Roles> roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "Kin")
    private KinEnum kin;

    public AppUser() {
    }

    public AppUser(String id, String login) {
        this.id = Long.valueOf(id);
        this.login = login;
    }

    public AppUser(Long id, Long partnerId) {
        this.id = id;
        this.partnerId = partnerId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "login", unique = true, nullable = false, length = 30)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "password", nullable = false, length = 100)
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_ROLES",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Column
    private Long partnerId;

    @Column(name = "confirmed")
    private boolean confirmed;

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public AppUser(UserDTO userDTO) {
        this.id = userDTO.getId() != null ? userDTO.getId() : null;
        this.profile = new UserProfile(userDTO.getProfile());
        this.login = userDTO.getLogin();
        this.roles = convertRoles(userDTO.getRoles());
        this.kin = userDTO.getKin() == null ? null : KinEnum.valueOf(userDTO.getKin().toUpperCase());
        this.partnerId = userDTO.getPartnerId();
        this.password = userDTO.getPassword();
    }

    private List<Roles> convertRoles(List<RoleDTO> dtoList) {
        if (null == dtoList) {
            return null;
        }
        return dtoList.stream().map(Roles::new).collect(Collectors.toList());
    }

    private UserProfile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
