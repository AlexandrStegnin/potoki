package com.art.model.supporting.dto;

import com.art.model.TransactionLog;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
public class TransactionLogDTO {

    private Long id;

    private String createdBy;

    private String txDate;

    private String investor;

    private String type;

    public TransactionLogDTO(TransactionLog entity) {
        this.id = entity.getId();
        this.createdBy = entity.getCreatedBy();
        this.txDate = getFormattedTxDate(entity.getTxDate());
        this.investor = entity.getInvestor();
        this.type = entity.getType().getTitle();
    }

    private String getFormattedTxDate(Date txDate) {
        if (null == txDate) return "";
        String localDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = dateFormat.format(txDate);
        } catch (Exception ignored) {
        }
        return localDate;
    }

}
