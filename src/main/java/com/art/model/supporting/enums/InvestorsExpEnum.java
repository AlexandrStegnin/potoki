package com.art.model.supporting.enums;

public enum InvestorsExpEnum {

    EMPTY(""),
    CONSTANT("Постоянный"),
    TEMP("Временный");
    private final String val;

    InvestorsExpEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
