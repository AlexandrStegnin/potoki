package com.art.model;

import com.art.model.supporting.dto.CashSourceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cash_source")
public class CashSource {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_source_generator")
  @SequenceGenerator(name = "cash_source_generator", sequenceName = "cash_source_id_seq")
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "organization_id")
  private String organizationId;

  public CashSource(CashSourceDTO dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.organizationId = dto.getOrganizationId();
  }

}
