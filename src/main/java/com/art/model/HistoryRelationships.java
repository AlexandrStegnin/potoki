package com.art.model;

import com.art.model.supporting.InvestorsSummary;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SqlResultSetMapping(
        name = "InvestorsSummaryMapping",
        classes = @ConstructorResult(
                targetClass = InvestorsSummary.class,
                columns = {
                        @ColumnResult(name = "facility", type = String.class),
                        @ColumnResult(name = "end_date", type = Date.class),
                        @ColumnResult(name = "aCorTag", type = String.class),
                        @ColumnResult(name = "fact_pay", type = float.class),
                        @ColumnResult(name = "ostatok_posle_rashodov", type = float.class),
                        @ColumnResult(name = "ostatok_posle_nalogov", type = float.class),
                        @ColumnResult(name = "ostatok_posle_vivoda", type = float.class),
                        @ColumnResult(name = "ostatok_po_dole", type = float.class),
                        @ColumnResult(name = "summ", type = float.class),
                        @ColumnResult(name = "pribil_s_prodazhi", type = float.class)
                }))
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "InvestorsSummary",
                query = "SELECT facility, end_date, aCorTag, fact_pay, ostatok_posle_rashodov, " +
                        "ostatok_posle_nalogov, ostatok_posle_vivoda, ostatok_po_dole, summ, pribil_s_prodazhi " +
                        "FROM InvestorsSummaryForIDEA " +
                        "WHERE InvestorId = ? " +
                        "ORDER BY facility, end_date DESC",
                resultClass = InvestorsSummary.class,
                resultSetMapping = "InvestorsSummaryMapping"),
        @NamedNativeQuery(
                name = "InvestorsSummaryWithFacility",
                query = "SELECT facility, end_date, aCorTag, fact_pay, ostatok_posle_rashodov, " +
                        "ostatok_posle_nalogov, ostatok_posle_vivoda, ostatok_po_dole, summ, pribil_s_prodazhi " +
                        "FROM InvestorsSummaryForIDEA " +
                        "WHERE InvestorId = ? AND facility = ?" +
                        "ORDER BY facility, end_date DESC",
                resultClass = InvestorsSummary.class,
                resultSetMapping = "InvestorsSummaryMapping")
})
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "HISTORY_RELATIONSHIPS")
public class HistoryRelationships implements Serializable{

    private static final Locale RUSSIAN_LOCAL = new Locale("ru", "RU");

    private BigInteger id;
    //@Temporal(TemporalType.DATE)
    private Date period;
    //private Date pay_date_agreement;
    //private float summ_agreement;
    //private float summ_reserv;
    //private Date pay_date_delay;
    //@Temporal(TemporalType.DATE)
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pay_date_fact;
    private float summ_fact;
    //@Temporal(TemporalType.DATE)
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    //private Date date_transfer_cash;
    //private float summ_transfer_cash;
    //private Date date_payment_bill;
    //private float summ_payment_bill;

    private Users manager;
    private Users rentor;
    private Facilities facility;
    //private InvestorsLoad investorsLoad;
    private PaymentsMethod paymentsMethod;
    private PaymentsType paymentsType;
    //private Set<Discounts> discounts;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "id")
    public Users getManager(){
        return manager;
    }
    public void setManager(Users manager){
        this.manager = manager;
    }

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "RENTOR_ID", referencedColumnName = "id")
    public Users getRentor(){
        return rentor;
    }
    public void setRentor(Users rentor){
        this.rentor = rentor;
    }

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "ID")
    public Facilities getFacility(){
        return facility;
    }
    public void setFacility(Facilities facility){
        this.facility = facility;
    }


    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PAYMENT_METHOD_ID", referencedColumnName = "ID")
    public PaymentsMethod getPaymentsMethod(){
        return paymentsMethod;
    }
    public void setPaymentsMethod(PaymentsMethod paymentsMethod){
        this.paymentsMethod = paymentsMethod;
    }

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PAY_TYPE_ID", referencedColumnName = "ID")
    public PaymentsType getPaymentsType(){
        return paymentsType;
    }
    public void setPaymentsType(PaymentsType paymentsType){
        this.paymentsType = paymentsType;
    }

    @Transient
    public String getPeriodToLocalDate(){
        String localDate;
        SimpleDateFormat formatMonth = new SimpleDateFormat("MMMM yyyy", RUSSIAN_LOCAL);
        //SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        try{
            localDate = formatMonth.format(period);
            //localDate = localDate + ' ' + formatYear.format(period);
        }catch (Exception ex){
            localDate = "";
        }
        return localDate;
    }

    @Transient
    public String getDateFactToLocalDate(){
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        localDate = format.format(pay_date_fact);
        return localDate;
    }

    /*
    @Transient
    public String getDateTransferCashToLocalDate(){
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(date_transfer_cash);
        }catch (Exception ex){
            localDate = "";
        }

        return localDate;
    }
    */
    /*
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "INVESTOR_LOAD_ID")
    public InvestorsLoad getInvestorsLoad(){
        return investorsLoad;
    }
    public void setInvestorsLoad(InvestorsLoad investorsLoad){
        this.investorsLoad = investorsLoad;
    }
    */


    /*
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "DISCOUNTS_ID")
    public Set<Discounts> getDiscounts(){
        return discounts;
    }
    public void setDiscounts(Set<Discounts> discounts){
        this.discounts = discounts;
    }
    */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "PERIOD")
    public Date getPeriod() {
        return period;
    }
    public void setPeriod(Date period) {
        this.period = period;
    }

    /*
    @Column(name = "PAY_DATE_AGREEMENT")
    public Date getPay_date_agreement() {
        return pay_date_agreement;
    }
    public void setPay_date_agreement(Date pay_date_agreement) {
        this.pay_date_agreement = pay_date_agreement;
    }


    @Column(name = "SUMM_AGREEMENT")
    public float getSumm_agreement() {
        return summ_agreement;
    }
    public void setSumm_agreement(float summ_agreement) {
        this.summ_agreement = summ_agreement;
    }

    @Column(name = "SUMM_RESERV")
    public float getSumm_reserv() {
        return summ_reserv;
    }
    public void setSumm_reserv(float summ_reserv) {
        this.summ_reserv = summ_reserv;
    }

    @Column(name = "PAY_DATE_DELAY")
    public Date getPay_date_delay() {
        return pay_date_delay;
    }
    public void setPay_date_delay(Date pay_date_delay) {
        this.pay_date_delay = pay_date_delay;
    }
    */

    @Column(name = "PAY_DATE_FACT")
    public Date getPay_date_fact() {
        return pay_date_fact;
    }
    public void setPay_date_fact(Date pay_date_fact) {
        this.pay_date_fact = pay_date_fact;
    }

    @Column(name = "SUMM_FACT")
    public float getSumm_fact() {
        return summ_fact;
    }
    public void setSumm_fact(float summ_fact) {
        this.summ_fact = summ_fact;
    }

    /*
    @Column(name = "DATE_TRANSFER_CASH")
    public Date getDate_transfer_cash() {
        return date_transfer_cash;
    }
    public void setDate_transfer_cash(Date date_transfer_cash) {
        this.date_transfer_cash = date_transfer_cash;
    }

    @Column(name = "SUMM_TRANSFER_CASH")
    public float getSumm_transfer_cash() {
        return summ_transfer_cash;
    }
    public void setSumm_transfer_cash(float summ_transfer_cash) {
        this.summ_transfer_cash = summ_transfer_cash;
    }
    */
    /*
    @Column(name = "DATE_PAYMENT_BILL")
    public Date getDate_payment_bill() {
        return date_payment_bill;
    }
    public void setDate_payment_bill(Date date_payment_bill) {
        this.date_payment_bill = date_payment_bill;
    }

    @Column(name = "SUMM_PAYMENT_BILL")
    public float getSumm_payment_bill() {
        return summ_payment_bill;
    }
    public void setSumm_payment_bill(float summ_payment_bill) {
        this.summ_payment_bill = summ_payment_bill;
    }
    */


}
