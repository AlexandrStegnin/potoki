package com.art.config.application;

/**
 * @author Alexandr Stegnin
 */

public class Location {

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

}
