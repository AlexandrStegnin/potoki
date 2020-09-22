package com.art.model.supporting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SalePaymentDTO extends PaymentDTO {

    private List<Long> salePaymentsId;

    private Date dateGiven;

    private Long facilityId;

    private Long underFacilityId;

    private String shareType;

}
