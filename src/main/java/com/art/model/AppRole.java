package com.art.model;

import com.art.model.supporting.dto.AppRoleDTO;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_role")
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 30)
    private String name;

    @Column(name = "humanized")
    private String humanized;

    public AppRole(AppRoleDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.humanized = dto.getHumanized();
    }
}
