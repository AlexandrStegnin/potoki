package com.art.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time")
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time")
    private Date modifiedTime;

    @PrePersist
    public void prePersist() {
        if (this.creationTime == null) {
            this.creationTime = new Date();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedTime = new Date();
    }

}
