package com.art.model;

import com.art.model.supporting.dto.RoleDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
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

    public Roles(RoleDTO dto) {
        this.id = Math.toIntExact(dto.getId());
        this.role = dto.getName();
    }
}
