package com.art.model.supporting;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ServiceUnavailable")
public class ServiceUnavailable implements Serializable {
    private BigInteger id;
    private int status;
    private Date startService;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "Status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "StartService")
    public Date getStartService() {
        return startService;
    }

    public void setStartService(Date startService) {
        this.startService = startService;
    }
}
