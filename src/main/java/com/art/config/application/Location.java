package com.art.config.application;

/**
 * @author Alexandr Stegnin
 */

public class Location {

    // Общие
    public static final String LOGIN = "/login";

    public static final String LOGOUT = "/logout";

    public static final String DEMO = "/demo";

    public static final String INVESTMENTS = "/investments";

    public static final String HOME = "/";

    public static final String WELCOME = "/welcome";

    public static final String WILD_CARD = "**";

    // Админ
    public static final String ADMIN = "/admin";

    public static final String CATALOGUE = "/catalogue";

    public static final String UPDATE_INV_DEMO = "/update-inv-demo";

    // Транзакции
    public static final String URL_TRANSACTIONS = "/transactions";

    public static final String URL_TRANSACTIONS_TX_ID = URL_TRANSACTIONS + "/{txId}";

    public static final String URL_TRANSACTIONS_ROLLBACK = URL_TRANSACTIONS + "/rollback";

    public static final String URL_TRANSACTIONS_CASH = URL_TRANSACTIONS + "/cash";

    // Объекты
    public static final String FACILITIES = "/facilities";

    public static final String FACILITIES_LIST = FACILITIES + "/list";

    public static final String FACILITIES_CREATE = FACILITIES + "/create";

    public static final String FACILITIES_UPDATE = FACILITIES + "/update";

    public static final String FACILITIES_DELETE = FACILITIES + "/delete";

    public static final String FACILITIES_EDIT = FACILITIES + "/edit/{id}";

    // Помещения
    public static final String ROOMS = "/rooms";

    public static final String ROOMS_LIST = ROOMS + "/list";

    public static final String ROOMS_EDIT = ROOMS + "/edit/{id}";

    public static final String ROOMS_DELETE = ROOMS + "/delete/{id}";

    public static final String ROOMS_CREATE = ROOMS + "/create";

    // Подобъекты
    public static final String UNDER_FACILITIES = "/facilities/child";

    public static final String UNDER_FACILITIES_LIST = UNDER_FACILITIES + "/list";

    public static final String UNDER_FACILITIES_EDIT = UNDER_FACILITIES + "/edit/{id}";

    public static final String UNDER_FACILITIES_DELETE = UNDER_FACILITIES + "/delete/{id}";

    public static final String UNDER_FACILITIES_CREATE = UNDER_FACILITIES + "/create";

    // Источники денег
    public static final String CASH_SOURCES = "/cash-sources";

    public static final String CASH_SOURCES_LIST = CASH_SOURCES + "/list";

    public static final String CASH_SOURCES_EDIT = CASH_SOURCES + "/edit/{id}";

    public static final String CASH_SOURCES_DELETE = CASH_SOURCES + "/delete";

    public static final String CASH_SOURCES_CREATE = CASH_SOURCES + "/create";

    // Приложения к договорам инвесторов
    public static final String INVESTOR_ANNEXES = "/investor/annexes";

    public static final String INVESTOR_ANNEXES_UPLOAD = INVESTOR_ANNEXES + "/upload";

    public static final String INVESTOR_ANNEXES_DELETE = INVESTOR_ANNEXES + "/delete";

    public static final String INVESTOR_ANNEXES_DELETE_LIST = INVESTOR_ANNEXES_DELETE + "/list";

    // Токены приложений
    public static final String TOKENS = "/tokens";

    public static final String TOKENS_EDIT_ID = "/edit-token-{id}";

    public static final String TOKENS_DELETE_ID = "/delete-token-{id}";

    public static final String TOKENS_GENERATE = "/generate";

    // Битрикс
    public static final String BITRIX_MERGE = "/bitrix/merge";

    // Websocket
    public static final String[] WEBSOCKET_PATHS = {
            "/turn" + WILD_CARD, "/progress" + WILD_CARD, "/status" + WILD_CARD
    };

    // Деньги инвесторов
    public static final String INVESTOR_CASH = "/investor-cash";

    public static final String INVESTOR_CASH_LIST = INVESTOR_CASH + "/list";

    public static final String INVESTOR_CASH_CREATE = INVESTOR_CASH + "/create";

    public static final String INVESTOR_CASH_EDIT_ID = INVESTOR_CASH + "/edit/{id}";

    public static final String INVESTOR_CASH_CLOSE_ID = INVESTOR_CASH + "/close/{id}";

    public static final String INVESTOR_CASH_SAVE = INVESTOR_CASH + "/save";

    public static final String INVESTOR_CASH_DOUBLE_ID = INVESTOR_CASH + "/double/{id}";

    public static final String INVESTOR_CASH_DELETE_LIST = INVESTOR_CASH + "/delete/list";

    public static final String INVESTOR_CASH_CASHING = INVESTOR_CASH + "/cashing";

    public static final String INVESTOR_CASH_CASHING_ALL = INVESTOR_CASH_CASHING + "/all";

    public static final String INVESTOR_CASH_GET = INVESTOR_CASH + "/get";

    public static final String INVESTOR_CASH_REINVEST_SAVE = INVESTOR_CASH + "/reinvest/save";

    public static final String INVESTOR_CASH_REINVEST_CASH_SAVE = INVESTOR_CASH + "/reinvest/cash/save";

    public static final String INVESTOR_CASH_DIVIDE_MULTIPLE = INVESTOR_CASH + "/divide/multiple";

    public static final String INVESTOR_CASH_DIVIDE_CASH_SAVE = INVESTOR_CASH + "/divide/cash/save";

    public static final String INVESTOR_CASH_CLOSE = INVESTOR_CASH + "/close";

    // Детали новых денег
    public static final String NEW_CASH_DETAILS = "/new-cash-details";

    public static final String NEW_CASH_DETAILS_LIST = NEW_CASH_DETAILS + "/list";

    public static final String NEW_CASH_DETAILS_EDIT = NEW_CASH_DETAILS + "/edit/{id}";

    public static final String NEW_CASH_DETAILS_DELETE = NEW_CASH_DETAILS + "/delete";

    public static final String NEW_CASH_DETAILS_CREATE = NEW_CASH_DETAILS + "/create";

    // Виды закрытия
    public static final String TYPE_CLOSING = "/type-closing";

    public static final String TYPE_CLOSING_LIST = TYPE_CLOSING + "/list";

    public static final String TYPE_CLOSING_EDIT = TYPE_CLOSING + "/edit/{id}";

    public static final String TYPE_CLOSING_DELETE = TYPE_CLOSING + "/delete";

    public static final String TYPE_CLOSING_CREATE = TYPE_CLOSING + "/create";

    public static final String[] ADMIN_URLS = {
            ADMIN,
            CATALOGUE,
            UPDATE_INV_DEMO,
            URL_TRANSACTIONS + WILD_CARD,
            FACILITIES + WILD_CARD,
            ROOMS + WILD_CARD,
            UNDER_FACILITIES + WILD_CARD,
            CASH_SOURCES + WILD_CARD,
            INVESTOR_ANNEXES + WILD_CARD,
            HOME, WELCOME, INVESTMENTS,
            TOKENS + WILD_CARD,
            TOKENS_GENERATE,
            BITRIX_MERGE,
            INVESTOR_CASH + WILD_CARD,
            NEW_CASH_DETAILS + WILD_CARD,
            TYPE_CLOSING + WILD_CARD
    };

    public static final String[] INVESTOR_URLS = {
            HOME, WELCOME, INVESTMENTS
    };
}
