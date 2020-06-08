package com.art.config.application;

/**
 * @author Alexandr Stegnin
 */

public class Location {

    public static final String URL_TRANSACTIONS = "/transactions";

    public static final String URL_TRANSACTIONS_TX_ID = URL_TRANSACTIONS + "/{txId}";

    public static final String URL_TRANSACTIONS_TX_ID_ROLLBACK = URL_TRANSACTIONS_TX_ID + "/rollback";

}
