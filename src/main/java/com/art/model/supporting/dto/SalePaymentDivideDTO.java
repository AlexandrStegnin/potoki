package com.art.model.supporting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SalePaymentDivideDTO extends PaymentDTO {

    private Long salePaymentId;

    private BigDecimal extractedSum;

}
