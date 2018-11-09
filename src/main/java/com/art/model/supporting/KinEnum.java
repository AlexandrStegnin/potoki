package com.art.model.supporting;

public enum KinEnum {
    KIN("Родственник"),
    NO_KIN("Не родственник"),
    SPOUSE("Супруг/супруга");

    private final String val;

    KinEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
