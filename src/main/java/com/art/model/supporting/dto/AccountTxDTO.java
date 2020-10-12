package com.art.model.supporting.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class AccountTxDTO {

    private List<Long> txIds;

}
