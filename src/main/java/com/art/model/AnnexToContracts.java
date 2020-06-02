package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "AnnexToContracts")
public class AnnexToContracts implements Serializable {

    private BigInteger id;
    private String annexName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "AnnexName")
    public String getAnnexName() {
        return annexName;
    }

    @Column(name = "FilePath")
    private String path;

    private Date dateLoad;

    @Column(name = "LoadedBy")
    private BigInteger loadedBy;

    @Column(name = "DateLoad")
    public Date getDateLoad() {
        return dateLoad;
    }

    public void setDateLoad(Date dateLoad) {
        this.dateLoad = dateLoad;
    }

    public BigInteger getLoadedBy() {
        return loadedBy;
    }

    public void setLoadedBy(BigInteger loadedBy) {
        this.loadedBy = loadedBy;
    }

    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
