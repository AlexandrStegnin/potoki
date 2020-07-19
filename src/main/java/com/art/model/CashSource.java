package com.art.model;

import com.art.model.supporting.dto.CashSourceDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "cash_source")
public class CashSource implements Serializable {
    private Long id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public CashSource() {}

    public CashSource(CashSourceDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
    }

}
