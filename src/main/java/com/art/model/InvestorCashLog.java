package com.art.model;

import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.ShareType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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

    @OneToOne
    @JoinColumn(name = "investor_id")
    private Users investor;

    @OneToOne
    @JoinColumn(name = "facility_id")
    private Facilities facility;

    @OneToOne
    @JoinColumn(name = "under_facility_id")
    private UnderFacilities underFacility;

    @Column(name = "given_cash")
    private BigDecimal givenCash;

    @Column(name = "date_given_cash")
    private Date dateGivenCash;

    @OneToOne
    @JoinColumn(name = "cash_source_id")
    private CashSources cashSource;

    @OneToOne
    @JoinColumn(name = "new_cash_detail_id")
    private NewCashDetails newCashDetail;

    @Column(name = "date_closing_invest")
    private Date dateClosingInvest;

    @OneToOne
    @JoinColumn(name = "type_closing_invest_id")
    private TypeClosingInvest typeClosingInvest;

    @Enumerated(EnumType.STRING)
    @Column(name = "share_type")
    private ShareType shareType;

    @Column(name = "date_report")
    private Date dateReport;

    @OneToOne
    @JoinColumn(name = "source_facility_id")
    private Facilities sourceFacility;

    @OneToOne
    @JoinColumn(name = "source_under_facility_id")
    private UnderFacilities sourceUnderFacility;

    @Column(name = "source_flows_id")
    private String sourceFlowsId;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Rooms room;

    @Column(name = "reinvest")
    private int reinvest;

    @Column(name = "source_id")
    private BigInteger sourceId;

    @Column(name = "source")
    private String source;

    @Column(name = "divide")
    private int divide;

    @Column(name = "real_date_given")
    private Date realDateGiven;

    @OneToOne
    @JoinColumn(name = "tx_id")
    private TransactionLog transactionLog;

    @Enumerated
    @Column(name = "instance_of")
    private CashType instanceOf;

    public InvestorCashLog(InvestorsCash cash, TransactionLog log, CashType instanceOf) {
        this.cashId = cash.getId().longValue();
        this.investor = cash.getInvestor();
        this.facility = cash.getFacility();
        this.underFacility = cash.getUnderFacility();
        this.givenCash = cash.getGivedCash();
        this.dateGivenCash = cash.getDateGivedCash();
        this.cashSource = cash.getCashSource();
        this.newCashDetail = cash.getNewCashDetails();
        this.dateClosingInvest = cash.getDateClosingInvest();
        this.typeClosingInvest = cash.getTypeClosingInvest();
        this.shareType = cash.getShareType();
        this.dateReport = cash.getDateReport();
        this.sourceFacility = cash.getSourceFacility();
        this.sourceUnderFacility = cash.getSourceUnderFacility();
        this.sourceFlowsId = cash.getSourceFlowsId();
        this.room = cash.getRoom();
        this.reinvest = cash.getIsReinvest();
        this.sourceId = cash.getSourceId();
        this.source = cash.getSource();
        this.divide = cash.getIsDivide();
        this.realDateGiven = cash.getRealDateGiven();
        this.transactionLog = log;
        this.instanceOf = instanceOf;
    }

    public InvestorCashLog(InvestorsFlowsSale flowsSale, TransactionLog log, CashType instanceOf) {
        this.cashId = flowsSale.getId().longValue();
        this.investor = flowsSale.getInvestor();
        this.facility = flowsSale.getFacility();
        this.dateGivenCash = flowsSale.getDateGived();
        this.givenCash = flowsSale.getProfitToReInvest();
        this.transactionLog = log;
        this.instanceOf = instanceOf;
    }

    public InvestorCashLog(InvestorsFlows flows, TransactionLog log, CashType instanceOf) {
        this.cashId = flows.getId().longValue();
        this.investor = flows.getInvestor();
        this.facility = flows.getFacility();
        this.dateGivenCash = flows.getReportDate();
        this.givenCash = BigDecimal.valueOf(flows.getAfterCashing());
        this.transactionLog = log;
        this.instanceOf = instanceOf;
    }

}
