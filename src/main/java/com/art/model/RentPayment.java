package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rent_payment")
@EqualsAndHashCode(exclude = {"facility", "investor", "underFacility", "transaction"})
@ToString(exclude = "transaction")
public class RentPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_report")
    private Date dateReport;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    private Facility facility;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "under_facility_id", referencedColumnName = "id")
    private UnderFacility underFacility;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "investor_id", referencedColumnName = "id")
    private AppUser investor;

    @Column(name = "share_type")
    private String shareType;

    @Column(name = "given_cash")
    private float givenCash;

    @Column(name = "share")
    private float share;

    @Column(name = "taxation")
    private float taxation;

    @Column(name = "cashing")
    private float cashing;

    @Column(name = "summa")
    private float summa;

    @Column(name = "sum_in_under_facility")
    private float sumInUnderFacility;

    @Column(name = "share_for_svod")
    private float shareForSvod;

    @Column(name = "on_investor")
    private float onInvestor;

    @Column(name = "after_tax")
    private float afterTax;

    @Column(name = "after_deduction_empty_facility")
    private float afterDeductionEmptyFacility;

    @Column(name = "after_cashing")
    private float afterCashing;

    @Column(name = "reinvest")
    private String reInvest;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "re_facility_id", referencedColumnName = "id")
    private Facility reFacility;

    @Column(name = "is_reinvest")
    private int isReinvest;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @Column(name = "acc_tx_id")
    private Long accTxId;

    @Transient
    public String getDateReportFormatted() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateReport);
        } catch (Exception ignored) {
        }

        return localDate;
    }

}
