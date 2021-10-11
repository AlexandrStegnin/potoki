package com.art.model;

import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.ShareType;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "investor_cash_log_generator")
  @SequenceGenerator(name = "investor_cash_log_generator", sequenceName = "investor_cash_log_id_seq")
  @Column(name = "id")
  private Long id;

  @Column(name = "cash_id")
  private Long cashId;

  @OneToOne
  @JoinColumn(name = "investor_id")
  private AppUser investor;

  @OneToOne
  @JoinColumn(name = "facility_id")
  private Facility facility;

  @OneToOne
  @JoinColumn(name = "under_facility_id")
  private UnderFacility underFacility;

  @Column(name = "given_cash")
  private BigDecimal givenCash;

  @Column(name = "date_given_cash")
  private Date dateGivenCash;

  @OneToOne
  @JoinColumn(name = "cash_source_id")
  private CashSource cashSource;

  @OneToOne
  @JoinColumn(name = "new_cash_detail_id")
  private NewCashDetail newCashDetail;

  @Column(name = "date_closing_invest")
  private Date dateClosingInvest;

  @OneToOne
  @JoinColumn(name = "type_closing_invest_id")
  private TypeClosing typeClosing;

  @Enumerated(EnumType.STRING)
  @Column(name = "share_type")
  private ShareType shareType;

  @Column(name = "date_report")
  private Date dateReport;

  @OneToOne
  @JoinColumn(name = "source_facility_id")
  private Facility sourceFacility;

  @OneToOne
  @JoinColumn(name = "source_under_facility_id")
  private UnderFacility sourceUnderFacility;

  @Column(name = "source_flows_id")
  private String sourceFlowsId;

  @OneToOne
  @JoinColumn(name = "room_id")
  private Room room;

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

  @OneToOne
  @JoinColumn(name = "tx_id")
  private TransactionLog transactionLog;

  @Enumerated
  @Column(name = "instance_of")
  private CashType instanceOf;

  public InvestorCashLog(Money cash, TransactionLog log, CashType instanceOf) {
    this.cashId = cash.getId();
    this.investor = cash.getInvestor();
    this.facility = cash.getFacility();
    this.underFacility = cash.getUnderFacility();
    this.givenCash = cash.getGivenCash();
    this.dateGivenCash = cash.getDateGiven();
    this.cashSource = cash.getCashSource();
    this.newCashDetail = cash.getNewCashDetail();
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
    this.transactionLog = log;
    this.instanceOf = instanceOf;
  }

  public InvestorCashLog(SalePayment flowsSale, TransactionLog log, CashType instanceOf) {
    this.cashId = flowsSale.getId();
    this.investor = flowsSale.getInvestor();
    this.facility = flowsSale.getFacility();
    this.dateGivenCash = flowsSale.getDateGiven();
    this.givenCash = flowsSale.getProfitToReInvest();
    this.transactionLog = log;
    this.instanceOf = instanceOf;
    this.shareType = flowsSale.getShareType();
  }

  public InvestorCashLog(RentPayment payment, TransactionLog log, CashType instanceOf) {
    this.cashId = payment.getId();
    this.investor = payment.getInvestor();
    this.facility = payment.getFacility();
    this.dateGivenCash = payment.getDateReport();
    this.givenCash = BigDecimal.valueOf(payment.getAfterCashing());
    this.transactionLog = log;
    this.instanceOf = instanceOf;
    this.shareType = ShareType.fromTitle(payment.getShareType());
  }

}
