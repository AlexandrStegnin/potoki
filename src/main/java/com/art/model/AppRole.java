package com.art.model;

import com.art.model.supporting.dto.AppRoleDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "app_role")
public class AppRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 30)
    private String name;

    public AppRole(AppRoleDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
    }
}