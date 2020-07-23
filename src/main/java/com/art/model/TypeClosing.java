package com.art.model;

import com.art.model.supporting.dto.TypeClosingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "type_closing")
public class TypeClosing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public TypeClosing(TypeClosingDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
    }

}
