package com.art.model;


import com.art.model.supporting.dto.RoleDTO;
import com.art.model.supporting.dto.UserDTO;
import com.art.model.supporting.enums.KinEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "USERS")
@ToString(of = {"id", "login", "email"})
@EqualsAndHashCode(of = {"id", "login", "email"})
public class Users implements Serializable {
    private BigInteger id;
    private String login;
    private String password;
    private String lastName;
    private String first_name;
    private String middle_name;
    private String email;
    private List<Roles> roles;
    private Set<Facilities> facilities;

    @Enumerated(EnumType.STRING)
    @Column(name = "Kin")
    private KinEnum kin;

    private List<UsersAnnexToContracts> usersAnnexToContractsList;

    public Users() {
    }

    public Users(String id, String login) {
        this.id = new BigInteger(id);
        this.login = login;
    }

    public Users(BigInteger id, BigInteger partnerId) {
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
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "first_name", length = 30)
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @Column(name = "middle_name", length = 30)
    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    @Column(name = "email", length = 30)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    private BigInteger partnerId;

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

    private Account account;

    @OneToOne
    @JoinColumn(name = "account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Users(UserDTO userDTO) {
        this.id = userDTO.getId() != null ? BigInteger.valueOf(userDTO.getId()) : null;
        this.lastName = userDTO.getLastName();
        this.first_name = userDTO.getFirstName();
        this.middle_name = userDTO.getMiddleName();
        this.login = userDTO.getLogin();
        this.email = userDTO.getEmail();
        this.roles = convertRoles(userDTO.getRoles());
        this.kin = KinEnum.valueOf(userDTO.getKin().toUpperCase());
        this.partnerId = BigInteger.valueOf(userDTO.getPartnerId());
    }

    private List<Roles> convertRoles(List<RoleDTO> dtoList) {
        return dtoList.stream().map(Roles::new).collect(Collectors.toList());
    }
}
