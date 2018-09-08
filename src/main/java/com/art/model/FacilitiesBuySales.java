package com.art.model;

import com.art.model.supporting.BuySalesEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "FacilitiesBuySales")
public class FacilitiesBuySales implements Serializable {

    private BigInteger id;
    private float summa;
    private Date dateGived;
    private BuySalesEnum operationType;
    private Facilities facility;
    private UnderFacilities underFacility;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "OperationType")
    public BuySalesEnum getOperationType() {
        return operationType;
    }

    public void setOperationType(BuySalesEnum operationType) {
        this.operationType = operationType;
    }

    @OneToOne
    @JoinColumn(name = "UnderFacilityId", referencedColumnName = "ID")
    public UnderFacilities getUnderFacility() {
        return underFacility;
    }

    public void setFacility(UnderFacilities underFacility) {
        this.underFacility = underFacility;
    }

    @OneToOne
    @JoinColumn(name = "FacilityId", referencedColumnName = "ID")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    @Transient
    public String getDateGivedToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateGived);
        } catch (Exception ignored) {
        }

        return localDate;
    }

}
