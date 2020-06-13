package com.art.model.supporting.dto;

import com.art.model.InvestorCashLog;
import com.art.model.InvestorsCash;
import com.art.model.supporting.CashType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class InvestorCashDTO {

    private BigInteger id;

    private String investor;

    private String facility;

    private String dateGivenCash;

    private BigDecimal givenCash;

    private String cashType;

    public InvestorCashDTO(InvestorsCash cash) {
        this.id = cash.getId();
        this.investor = cash.getInvestor().getLogin();
        this.facility = cash.getFacility().getFacility();
        this.dateGivenCash = convertDate(cash.getDateGivedCash());
        this.givenCash = cash.getGivedCash();
        this.cashType = CashType.NEW.getTitle();
    }

    public InvestorCashDTO(InvestorCashLog cashLog) {
        this.id = BigInteger.valueOf(cashLog.getId());
        this.investor = cashLog.getInvestor().getLogin();
        this.facility = cashLog.getFacility().getFacility();
        this.dateGivenCash = convertDate(cashLog.getDateGivenCash());
        this.givenCash = cashLog.getGivenCash();
        this.cashType = CashType.OLD.getTitle();
    }

    private String convertDate(Date dateGivenCash) {
        if (null == dateGivenCash) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String localDate = "";
        try {
            localDate = formatter.format(dateGivenCash);
        } catch (Exception ignored) {}
        return localDate;
    }

    public void setDateGivenCash(Date dateGivenCash) {
        this.dateGivenCash = convertDate(dateGivenCash);
    }

    public BigDecimal getGivenCash() {
        return givenCash.setScale(2, BigDecimal.ROUND_CEILING);
    }
}
