package com.art.model;

import com.art.model.supporting.dto.AppRoleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_role")
public class AppRole {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_generator")
  @SequenceGenerator(name = "app_role_generator", sequenceName = "app_role_id_seq")
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

  @PrePersist
  public void prePersist() {
    this.name = this.name.toUpperCase();
    if (!this.name.startsWith("ROLE_")) {
      this.name = "ROLE_".concat(this.name);
    }
  }

}
