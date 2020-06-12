package com.art.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "investor_cash_log")
public class InvestorCashLog {

    @Id
    @TableGenerator(name = "invCashLogSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "INV.CASH.LOG.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "invCashLogSeqStore")
    @Column(name = "id")
    private Long id;

    @Column(name = "cash_id")
    private Long cashId;

    @Column(name = "investor_id")
    private Long investorId;

    @Column(name = "facility_id")
    private Long facilityId;

    @Column(name = "under_facility_id")
    private Long underFacilityId;

    @Column(name = "given_cash")
    private BigDecimal givenCash;

    @Column(name = "date_given_cash")
    private Date dateGivenCash;

    @Column(name = "cash_source_id")
    private Long cashSourceId;

    @Column(name = "cash_type_id")
    private Long cashTypeId;

    @Column(name = "new_cash_detail_id")
    private Long newCashDetailId;

    @Column(name = "investor_type_id")
    private Long investorTypeId;

    @Column(name = "date_closing_invest")
    private Date dateClosingInvest;

    @Column(name = "type_closing_invest_id")
    private Long typeClosingInvestId;

    @Column(name = "share_kind_id")
    private Long shareKindId;

    @Column(name = "date_report")
    private Date dateReport;

    @Column(name = "source_facility_id")
    private Long sourceFacilityId;

    @Column(name = "source_under_facility_id")
    private Long sourceUnderFacilityId;

    @Column(name = "source_flows_id")
    private String sourceFlowsId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "reinvest")
    private int reinvest;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source")
    private String source;

    @Column(name = "divide")
    private int divide;

    @Column(name = "real_date_given")
    private Date realDateGiven;

    public InvestorCashLog(InvestorsCash cash) {
        this.cashId = cash.getId().longValue();
        this.investorId = cash.getInvestor().getId().longValue();
        this.facilityId = cash.getFacility().getId().longValue();
        if (null != cash.getUnderFacility()) {
            this.underFacilityId = cash.getUnderFacility().getId().longValue();
        }
        this.givenCash = cash.getGivedCash();
        this.dateGivenCash = cash.getDateGivedCash();
        if (null != cash.getSourceId()) {
            this.cashSourceId = cash.getSourceId().longValue();
        }
        if (null != cash.getCashType()) {
            this.cashTypeId = cash.getCashType().getId().longValue();
        }
        if (null != cash.getNewCashDetails()) {
            this.newCashDetailId = cash.getNewCashDetails().getId().longValue();
        }
        if (null != cash.getInvestorsType()) {
            this.investorTypeId = cash.getInvestorsType().getId().longValue();
        }
        this.dateClosingInvest = cash.getDateClosingInvest();
        if (null != cash.getTypeClosingInvest()) {
            this.typeClosingInvestId = cash.getTypeClosingInvest().getId().longValue();
        }
        if (null != cash.getShareKind()) {
            this.shareKindId = cash.getShareKind().getId().longValue();
        }
        this.dateReport = cash.getDateReport();
        if (null != cash.getSourceFacility()) {
            this.sourceFacilityId = cash.getSourceFacility().getId().longValue();
        }
        if (null != cash.getSourceUnderFacility()) {
            this.sourceUnderFacilityId = cash.getSourceUnderFacility().getId().longValue();
        }
        this.sourceFlowsId = cash.getSourceFlowsId();
        if (null != cash.getRoom()) {
            this.roomId = cash.getRoom().getId().longValue();
        }
        this.reinvest = cash.getIsReinvest();
        if (null != cash.getSourceId()) {
            this.sourceId = cash.getSourceId().longValue();
        }
        this.source = cash.getSource();
        this.divide = cash.getIsDivide();
        this.realDateGiven = cash.getRealDateGiven();
    }

}
