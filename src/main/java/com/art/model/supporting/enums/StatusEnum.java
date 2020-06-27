package com.art.model.supporting.enums;

public enum StatusEnum {
    ACTIVE("Активен"),
    NO_ACTIVE("Не активен");

    private final String val;

    StatusEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
