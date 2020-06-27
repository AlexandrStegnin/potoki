package com.art.model.supporting.dto;

import com.art.model.supporting.enums.KinEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingTreeDTO {

    private BigInteger partnerId;

    private BigInteger investorId;

    private KinEnum kin;

    private Date firstInvestmentDate;

    private String invStatus;

    private int daysToDeactivate;

    private int serNumber;

    private BigInteger parentPartnerId;

    public MarketingTreeDTO(BigInteger partnerId, BigInteger investorId, KinEnum kin, Date firstInvestmentDate,
                            String invStatus, int daysToDeactivate, int serNumber) {
        this.partnerId = partnerId;
        this.investorId = investorId;
        this.kin = kin;
        this.firstInvestmentDate = firstInvestmentDate;
        this.invStatus = invStatus;
        this.daysToDeactivate = daysToDeactivate;
        this.serNumber = serNumber;
    }

}
