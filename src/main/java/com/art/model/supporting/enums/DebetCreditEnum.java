package com.art.model.supporting.enums;

public enum DebetCreditEnum {

    EMPTY(""),
    DEBET("Дебет"),
    CREDIT("Кредит");
    private final String val;

    DebetCreditEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
