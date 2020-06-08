package com.art.model.supporting.filters;

import lombok.Data;

import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
public class TxLogFilter {

    private Date txDate;

    private String type;

    private String investor;

    private String creator;

}
