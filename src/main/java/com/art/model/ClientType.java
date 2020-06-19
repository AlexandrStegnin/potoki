package com.art.model;

import com.art.config.SecurityUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "client_type")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientType {

    @Id
    @TableGenerator(name = "clientTypeSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "CLIENT.TYPE.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "clientTypeSeqStore")
    @Column(name = "id")
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "creation_time")
    Date creationTime;

    @Column(name = "modified_time")
    Date modifiedTime;

    @Column(name = "modified_by")
    String modifiedBy;

    @PrePersist
    public void prePersist() {
        this.creationTime = new Date();
        this.modifiedBy = SecurityUtils.getUsername();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedTime = new Date();
        this.modifiedBy = SecurityUtils.getUsername();
    }

}
