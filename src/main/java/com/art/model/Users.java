package com.art.model;


import com.art.model.supporting.enums.KinEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"roles", "userStuff", "facilities", "usersAnnexToContractsList"})
@EqualsAndHashCode(exclude = {"stuffId", "password", "lastName", "first_name", "middle_name", "email",
        "roles", "userStuff", "facilities", "usersAnnexToContractsList"})
@Entity
@Table(name = "USERS")
public class Users implements Serializable {
    private BigInteger id;
    private String login;
    private BigInteger stuffId;
    private String password;
    private String lastName;
    private String first_name;
    private String middle_name;
    private String email;
//    private String state;
    private List<Roles> roles;
    private Stuffs userStuff;
//    private Set<MailingGroups> mailingGroups;
    private Set<Facilities> facilities;

    @Enumerated(EnumType.STRING)
    @Column(name = "Kin")
    private KinEnum kin;

//    @Transient
//    private transient List<BigInteger> facilityId;
//    @Transient
//    private transient List<String> facilityName;

    private List<UsersAnnexToContracts> usersAnnexToContractsList;
//    private Set<Emails> emails;

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

    @OneToOne(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY)
    @JoinColumn(name = "stuffId", referencedColumnName = "id")
    public Stuffs getUserStuff() {
        return userStuff;
    }

    public void setUserStuff(Stuffs userStuff) {
        this.userStuff = userStuff;
    }

//    @ManyToMany(cascade =
//            {
//                    CascadeType.DETACH,
//                    CascadeType.MERGE,
//                    CascadeType.REFRESH
//            },
//            fetch = FetchType.LAZY)
//    @JoinTable(name = "UsersMailingGroups",
//            joinColumns = {@JoinColumn(name = "UserId", referencedColumnName = "id")},
//            inverseJoinColumns = @JoinColumn(name = "MailingGroupsId", referencedColumnName = "id"))
//    public Set<MailingGroups> getMailingGroups() {
//        return mailingGroups;
//    }
//
//    public void setMailingGroups(Set<MailingGroups> mailingGroups) {
//        this.mailingGroups = mailingGroups;
//    }


    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY)
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

//    @Column(name = "state")
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }

//    @Transient
//    public String getFullName() {
//        return first_name +
//                " " +
//                middle_name;
//    }
//
    @Column(name = "stuffId", insertable = false, updatable = false)
    public BigInteger getStuffId() {
        return stuffId;
    }

    public void setStuffId(BigInteger stuffId) {
        this.stuffId = stuffId;
    }

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_ROLES",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Column
    private BigInteger partnerId;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY)
    @JoinTable(name = "UsersAnnexToContracts",
            joinColumns = {@JoinColumn(name = "UserId")},
            inverseJoinColumns = @JoinColumn(name = "Id")
    )
    public List<UsersAnnexToContracts> getUsersAnnexToContractsList() {
        return usersAnnexToContractsList;
    }

    public void setUsersAnnexToContractsList(List<UsersAnnexToContracts> usersAnnexToContractsList) {
        this.usersAnnexToContractsList = usersAnnexToContractsList;
    }

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    public Set<Emails> getEmails() {
//        return emails;
//    }
//
//    public void setEmails(Set<Emails> emails) {
//        this.emails = emails;
//    }
//
//    @Transient
//    public List<BigInteger> getFacilityId() {
//        return facilityId;
//    }

//    @Transient
//    public List<String> getFacilityName() {
//        return facilityName;
//    }

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
}
