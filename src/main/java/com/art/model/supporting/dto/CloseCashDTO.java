package com.art.model.supporting.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class CloseCashDTO {

    private List<Long> investorCashIdList = new ArrayList<>();

    private Long investorBuyerId;

    private Date dateReinvest;

    private String operation;

    private Date realDateGiven;

}
