package com.art.config.application;

/**
 * @author Alexandr Stegnin
 */

public class Location {

    public static final String ADMIN = "/admin";

    public static final String CATALOGUE = "/catalogue";

    public static final String UPDATE_INV_DEMO = "/update-inv-demo";

    public static final String URL_TRANSACTIONS = "/transactions";

    public static final String URL_TRANSACTIONS_TX_ID = URL_TRANSACTIONS + "/{txId}";

    public static final String URL_TRANSACTIONS_ROLLBACK = URL_TRANSACTIONS + "/rollback";

    public static final String URL_TRANSACTIONS_CASH = URL_TRANSACTIONS + "/cash";

    public static final String FACILITIES = "/facilities";

    public static final String FACILITIES_LIST = FACILITIES + "/list";

    public static final String FACILITIES_CREATE = FACILITIES + "/create";

    public static final String FACILITIES_UPDATE = FACILITIES + "/update";

    public static final String FACILITIES_DELETE = FACILITIES + "/delete";

    public static final String FACILITIES_EDIT = FACILITIES + "/edit/{id}";

    public static final String ROOMS = "/rooms";

    public static final String ROOMS_LIST = ROOMS + "/list";

    public static final String ROOMS_EDIT = ROOMS + "/edit/{id}";

    public static final String ROOMS_DELETE = ROOMS + "/delete/{id}";

    public static final String ROOMS_CREATE = ROOMS + "/create";

    public static final String UNDER_FACILITIES = "/facilities/child";

    public static final String UNDER_FACILITIES_LIST = UNDER_FACILITIES + "/list";

    public static final String UNDER_FACILITIES_EDIT = UNDER_FACILITIES + "/edit/{id}";

    public static final String UNDER_FACILITIES_DELETE = UNDER_FACILITIES + "/delete/{id}";

    public static final String UNDER_FACILITIES_CREATE = UNDER_FACILITIES + "/create";

    public static final String CASH_SOURCES = "/cash-sources";

    public static final String CASH_SOURCES_LIST = CASH_SOURCES + "/list";

    public static final String CASH_SOURCES_EDIT = CASH_SOURCES + "/edit/{id}";

    public static final String CASH_SOURCES_DELETE = CASH_SOURCES + "/delete";

    public static final String CASH_SOURCES_CREATE = CASH_SOURCES + "/create";

}
