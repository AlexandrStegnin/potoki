package com.art.config.application;

/**
 * @author Alexandr Stegnin
 */

public class Location {

    public static final String URL_TRANSACTIONS = "/transactions";

    public static final String URL_TRANSACTIONS_TX_ID = URL_TRANSACTIONS + "/{txId}";

    public static final String URL_TRANSACTIONS_ROLLBACK = URL_TRANSACTIONS + "/rollback";

    public static final String URL_TRANSACTIONS_CASH = URL_TRANSACTIONS + "/cash";

    public static final String CLIENT_TYPES = "/client/types";

    public static final String CLIENT_TYPES_CREATE = CLIENT_TYPES + "/create";

    public static final String CLIENT_TYPES_UPDATE = CLIENT_TYPES + "/update";

    public static final String CLIENT_TYPES_DELETE = CLIENT_TYPES + "/delete";


}
