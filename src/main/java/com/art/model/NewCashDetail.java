package com.art.model;

import com.art.model.supporting.dto.NewCashDetailDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "new_cash_detail")
public class NewCashDetail implements Serializable {

  private Long id;

  private String name;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_cash_detail_generator")
  @SequenceGenerator(name = "new_cash_detail_generator", sequenceName = "new_cash_detail_id_seq")
  @Column(name = "id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public NewCashDetail(NewCashDetailDTO dto) {
    this.id = dto.getId();
    this.name = dto.getName();
  }

}
