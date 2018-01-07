package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "ShareKind")
public class ShareKind {

    private BigInteger id;
    private String shareKind;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "ShareKind")
    public String getShareKind(){
        return shareKind;
    }
    public void setShareKind(String shareKind){
        this.shareKind = shareKind;
    }

}
