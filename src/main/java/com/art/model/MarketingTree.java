package com.art.model;

import com.art.model.supporting.KinEnum;
import com.art.model.supporting.StatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
@NamedStoredProcedureQuery(
        name = "updateMarketingTree",
        procedureName = "UpdateMarketingTree",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = BigInteger.class, name = "invId")
        }
)
@NoArgsConstructor
public class MarketingTree implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private BigInteger id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PartnerId", referencedColumnName = "Id")
    private Users partner;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "InvestorId", referencedColumnName = "Id")
    private Users investor;

    @Column
    @Enumerated(EnumType.STRING)
    private KinEnum kin;

    @Column
    private Date firstInvestmentDate;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum invStatus;

    @Column
    private int daysToDeactivate;

    @Column
    private int serNumber;

}
