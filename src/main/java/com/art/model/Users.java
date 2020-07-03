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
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "USERS")
@ToString(of = {"id", "login"})
@EqualsAndHashCode(of = {"id", "login"})
public class Users implements Serializable {

    private Long id;

    private String login;

    private String password;

    private List<Roles> roles;

    private Set<Facilities> facilities;

    @Enumerated(EnumType.STRING)
    @Column(name = "Kin")
    private KinEnum kin;

    private List<UsersAnnexToContracts> usersAnnexToContractsList;

    public Users() {
    }

    public Users(String id, String login) {
        this.id = Long.valueOf(id);
        this.login = login;
    }

    public Users(Long id, Long partnerId) {
        this.id = id;
        this.partnerId = partnerId;
    }

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_FACILITYES",
            joinColumns = {@JoinColumn(name = "RENTOR_INVESTORS_ID", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "FACILITY_ID", referencedColumnName = "id"))
    public Set<Facilities> getFacilities() {
        return facilities;
    }

    public void setFacilities(Set<Facilities> facilities) {
        this.facilities = facilities;
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

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST }, fetch = FetchType.EAGER)
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

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinTable(name = "UsersAnnexToContracts",
            joinColumns = {@JoinColumn(name = "UserId")},
            inverseJoinColumns = @JoinColumn(name = "Id"))
    public List<UsersAnnexToContracts> getUsersAnnexToContractsList() {
        return usersAnnexToContractsList;
    }

    public void setUsersAnnexToContractsList(List<UsersAnnexToContracts> usersAnnexToContractsList) {
        this.usersAnnexToContractsList = usersAnnexToContractsList;
    }

    @Column(name = "confirmed")
    private boolean confirmed;

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Users(UserDTO userDTO) {
        this.id = userDTO.getId() != null ? userDTO.getId() : null;
        this.profile = new UserProfile(userDTO.getProfile());
        this.login = userDTO.getLogin();
        this.roles = convertRoles(userDTO.getRoles());
        this.kin = KinEnum.valueOf(userDTO.getKin().toUpperCase());
        this.partnerId = userDTO.getPartnerId();
    }

    private List<Roles> convertRoles(List<RoleDTO> dtoList) {
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
