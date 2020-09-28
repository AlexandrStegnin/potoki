package com.art.model.supporting.enums;

public enum KinEnum {
    EMPTY(null),
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

    public static KinEnum fromValue(String val) {
        for (KinEnum kin : values()) {
            if (kin.getVal() != null) {
                if (kin.getVal().equalsIgnoreCase(val)) {
                    return kin;
                }
            }
        }
        return EMPTY;
    }

}
