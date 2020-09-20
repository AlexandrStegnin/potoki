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
public class RentPaymentDTO extends PaymentDTO {

    private Date dateGiven;

    private Long facilityId;

    private String shareType;

    private List<Long> rentPaymentsId;

}
