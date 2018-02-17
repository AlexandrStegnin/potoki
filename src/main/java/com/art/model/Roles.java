package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "ROLES")
public class Roles implements Serializable {
    private Integer id;
    private String role;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "role", unique = true, nullable = false, length = 30)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
