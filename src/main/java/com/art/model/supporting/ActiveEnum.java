package com.art.model.supporting;

public enum ActiveEnum {
    ACTIVE("Активный"),
    BLOCKED("Блокированный");
    private final String val;

    ActiveEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
