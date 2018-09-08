package com.art.model.supporting;

public enum BuySalesEnum {

    EMPTY(""),
    BUY("Покупка"),
    SALES("Продажа"),
    PLAN_BUY("Плановая покупка"),
    PLAN_SALES("Плановая продажа");
    private final String val;

    BuySalesEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
