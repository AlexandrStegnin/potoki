package com.art.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private Account account;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private Users user;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "patronymic")
    private String patronymic;

    private String email;

}
