package com.art.model;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Alexandr Stegnin
 */

@Entity
@Table(name = "app_token")
public class AppToken {

    @Id
    @TableGenerator(name = "appTokenSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "APP.TOKEN.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "appTokenSeqStore")
    @Column(name = "Id")
    private Long id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "token")
    private String token;

    public AppToken() {
        this.token = generateToken();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

}
