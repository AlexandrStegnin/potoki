package com.art.model.supporting.dto;

import com.art.model.TransactionLog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class TransactionLogDTO {

    private Long id;

    private String createdBy;

    private String txDate;

    private String type;

    private boolean rollbackEnabled;

    private Long blockedFrom;

    public TransactionLogDTO(TransactionLog entity) {
        this.id = entity.getId();
        this.createdBy = entity.getCreatedBy();
        this.txDate = getFormattedTxDate(entity.getTxDate());
        this.type = entity.getType().getTitle();
        this.rollbackEnabled = entity.isRollbackEnabled();
        this.blockedFrom = entity.getBlockedFrom() == null ? null : entity.getBlockedFrom().getId();
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
