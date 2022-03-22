package com.art.config.application;

import lombok.experimental.UtilityClass;

/**
 * @author Alexandr Stegnin
 */
@UtilityClass
public class Location {

  private final String LIST = "/list";
  private final String CREATE = "/create";
  private final String UPDATE = "/update";
  private final String DELETE = "/delete";
  private final String FIND = "/find";
  private final String REINVEST = "/reinvest";
  private final String UPLOAD = "/upload";

  // Общие
  public final String LOGIN = "/login";

  public final String LOGOUT = "/logout";

  public final String INVESTMENTS = "/investments";

  public final String HOME = "/";

  public final String WELCOME = "/welcome";

  public final String WILD_CARD = "**";

  // Админ
  public final String ADMIN = "/admin";

  public final String CATALOGUE = "/catalogue";

  // Транзакции
  public final String URL_TRANSACTIONS = "/transactions";

  public final String URL_TRANSACTIONS_TX_ID = URL_TRANSACTIONS + "/{txId}";

  public final String URL_TRANSACTIONS_ROLLBACK = URL_TRANSACTIONS + "/rollback";

  public final String URL_TRANSACTIONS_CASH = URL_TRANSACTIONS + "/cash";

  // Объекты
  public final String FACILITIES = "/facilities";

  public final String FACILITIES_LIST = FACILITIES + LIST;

  public final String FACILITIES_CREATE = FACILITIES + CREATE;

  public final String FACILITIES_UPDATE = FACILITIES + UPDATE;

  public final String FACILITIES_DELETE = FACILITIES + DELETE;

  public final String FACILITY_FIND = FACILITIES + FIND;

  public final String FACILITIES_OPENED = FACILITIES + "/opened";


  // Помещения
  public final String ROOMS = "/rooms";

  public final String ROOMS_LIST = ROOMS + LIST;

  public final String ROOMS_UPDATE = ROOMS + UPDATE;

  public final String ROOMS_DELETE = ROOMS + DELETE;

  public final String ROOMS_CREATE = ROOMS + CREATE;

  public final String ROOMS_FIND = ROOMS + FIND;

  // Подобъекты
  public final String UNDER_FACILITIES = "/facilities/child";

  public final String UNDER_FACILITIES_LIST = UNDER_FACILITIES + LIST;

  public final String UNDER_FACILITIES_UPDATE = UNDER_FACILITIES + UPDATE;

  public final String UNDER_FACILITIES_DELETE = UNDER_FACILITIES + DELETE;

  public final String UNDER_FACILITIES_CREATE = UNDER_FACILITIES + CREATE;

  public final String UNDER_FACILITIES_FIND = UNDER_FACILITIES + FIND;

  // Источники денег
  public final String CASH_SOURCES = "/cash-sources";

  public final String CASH_SOURCES_LIST = CASH_SOURCES + LIST;

  public final String CASH_SOURCES_UPDATE = CASH_SOURCES + UPDATE;

  public final String CASH_SOURCES_DELETE = CASH_SOURCES + DELETE;

  public final String CASH_SOURCES_CREATE = CASH_SOURCES + CREATE;

  public final String CASH_SOURCES_FIND = CASH_SOURCES + FIND;

  // Приложения к договорам инвесторов
  public final String INVESTOR_ANNEXES = "/investor/annexes";

  public final String INVESTOR_ANNEXES_UPLOAD = INVESTOR_ANNEXES + UPLOAD;

  public final String INVESTOR_ANNEXES_DELETE = INVESTOR_ANNEXES + DELETE;

  public final String INVESTOR_ANNEXES_DELETE_LIST = INVESTOR_ANNEXES_DELETE + LIST;

  // Токены приложений
  public final String TOKENS = "/tokens";

  public final String TOKENS_CREATE = TOKENS + CREATE;

  public final String TOKENS_UPDATE = TOKENS + UPDATE;

  public final String TOKENS_DELETE = TOKENS + DELETE;

  public final String TOKENS_FIND = TOKENS + FIND;

  // Битрикс
  public final String BITRIX_MERGE = "/bitrix/merge";

  // Websocket
  public final String[] WEBSOCKET_PATHS = {
      "/turn" + WILD_CARD, "/progress" + WILD_CARD, "/status" + WILD_CARD
  };

  // Деньги инвесторов
  public final String MONEY = "/money";

  public final String MONEY_LIST = MONEY + LIST;

  public final String MONEY_CREATE = MONEY + CREATE;

  public final String MONEY_EDIT_ID = MONEY + "/edit/{id}";

  public final String MONEY_DELETE_LIST = MONEY + "/delete/list";

  public final String MONEY_CASHING = MONEY + "/cashing";

  public final String MONEY_DIVIDE = "/divide-cash";

  public final String MONEY_DIVIDE_MULTIPLE = MONEY + "/divide-multiple";

  public final String MONEY_CLOSE = MONEY + "/close";

  public final String MONEY_UPDATE = MONEY + UPDATE;

  public final String MONEY_CLOSE_RESALE = MONEY + "/close/resale";

  public final String MONEY_ACCEPT = MONEY + "/accept";

  public final String MONEY_OPENED = MONEY + "/opened";

  public final String MONEY_RE_BUY = MONEY + "/buy-share";


  // Детали новых денег
  public final String NEW_CASH_DETAILS = "/new-cash-details";

  public final String NEW_CASH_DETAILS_LIST = NEW_CASH_DETAILS + LIST;

  public final String NEW_CASH_DETAILS_UPDATE = NEW_CASH_DETAILS + UPDATE;

  public final String NEW_CASH_DETAILS_DELETE = NEW_CASH_DETAILS + DELETE;

  public final String NEW_CASH_DETAILS_CREATE = NEW_CASH_DETAILS + CREATE;

  public final String NEW_CASH_DETAILS_FIND = NEW_CASH_DETAILS + FIND;

  // Виды закрытия
  public final String TYPE_CLOSING = "/type-closing";

  public final String TYPE_CLOSING_LIST = TYPE_CLOSING + LIST;

  public final String TYPE_CLOSING_UPDATE = TYPE_CLOSING + UPDATE;

  public final String TYPE_CLOSING_DELETE = TYPE_CLOSING + DELETE;

  public final String TYPE_CLOSING_CREATE = TYPE_CLOSING + CREATE;

  public final String TYPE_CLOSING_FIND = TYPE_CLOSING + FIND;

  // Восстановление пароля
  public final String FORGOT_PASSWORD = "/forgotPassword";

  public final String SAVE_PASSWORD = "/savePassword";

  public final String SW_JS = "/sw.js";

  public final String RESET_PASSWORD = "/resetPassword";

  public final String CHANGE_PASSWORD = "/changePassword";

  // Выплаты инвесторам по аренде/продаже
  public final String PAYMENTS_URL = "/payments";

  public final String RENT_PAYMENTS = PAYMENTS_URL + "/rent";

  public final String RENT_PAYMENTS_UPLOAD = RENT_PAYMENTS + UPLOAD;

  public final String RENT_PAYMENTS_REINVEST = RENT_PAYMENTS + REINVEST;

  public final String RENT_PAYMENTS_DELETE_CHECKED = RENT_PAYMENTS + "/delete/checked";

  public final String SALE_PAYMENTS = PAYMENTS_URL + "/sale";

  public final String SALE_PAYMENTS_UPLOAD = SALE_PAYMENTS + UPLOAD;

  public final String SALE_PAYMENTS_DELETE_CHECKED = SALE_PAYMENTS + "/delete/checked";

  public final String SALE_PAYMENTS_REINVEST = SALE_PAYMENTS + REINVEST;

  public final String SALE_PAYMENTS_DIVIDE = SALE_PAYMENTS + "/divide";

  // ПОЛЬЗОВАТЕЛИ
  public final String USERS_URL = "/users";

  public final String DEACTIVATE_USER = USERS_URL + "/deactivate";

  public final String USERS_LIST = USERS_URL + LIST;

  public final String USERS_SAVE = USERS_URL + "/save";

  public final String USERS_DELETE = USERS_URL + DELETE;

  public final String USERS_FIND_BY_ID = USERS_URL + FIND;

  //РОЛИ СИСТЕМЫ
  public final String ROLES_URL = "/roles";

  public final String ROLE_LIST = ROLES_URL + LIST;

  public final String ROLE_CREATE = ROLES_URL + CREATE;

  public final String ROLE_FIND = ROLES_URL + FIND;

  public final String ROLE_UPDATE = ROLES_URL + UPDATE;

  public final String ROLE_DELETE = ROLES_URL + DELETE;

  //ТРАНЗАКЦИИ ПО СЧЕТАМ
  public final String ACC_TRANSACTIONS = MONEY + "/transactions";

  public final String ACC_TRANSACTIONS_DELETE = ACC_TRANSACTIONS + DELETE;

  //СВОБОДНЫЕ СРЕДСТВА КЛИЕНТОВ
  public final String TRANSACTIONS_SUMMARY = ACC_TRANSACTIONS + "/summary";

  public final String TRANSACTIONS_DETAILS = ACC_TRANSACTIONS + "/details";

  public final String TRANSACTIONS_REINVEST = ACC_TRANSACTIONS + REINVEST;

  public final String TRANSACTIONS_BALANCE = ACC_TRANSACTIONS + "/balance";

  //МАРКЕТИНГОВОЕ ДЕРЕВО
  public final String MARKETING_TREE = "/marketing-tree";

  public final String MARKETING_TREE_UPDATE = MARKETING_TREE + UPDATE;

  //С КЕМ ЗАКЛЮЧЁН ДОГОВОР
  public final String USER_AGREEMENTS = "/agreements";

  public final String USER_AGREEMENTS_LIST = USER_AGREEMENTS + LIST;

  public final String SEND_WELCOME = "/send/welcome";

  public final String[] ADMIN_URLS = {
      ADMIN,
      CATALOGUE,
      URL_TRANSACTIONS + WILD_CARD,
      FACILITIES + WILD_CARD,
      ROOMS + WILD_CARD,
      UNDER_FACILITIES + WILD_CARD,
      CASH_SOURCES + WILD_CARD,
      INVESTOR_ANNEXES + WILD_CARD,
      HOME, WELCOME, INVESTMENTS,
      TOKENS + WILD_CARD,
      BITRIX_MERGE,
      MONEY + WILD_CARD,
      NEW_CASH_DETAILS + WILD_CARD,
      TYPE_CLOSING + WILD_CARD,
      MONEY_DIVIDE,
      MONEY_DIVIDE_MULTIPLE,
      MONEY + WILD_CARD,
      PAYMENTS_URL + WILD_CARD,
      USERS_URL + WILD_CARD,
      SEND_WELCOME
  };

  public final String[] PERMIT_ALL_URLS = {
      HOME,
      FORGOT_PASSWORD,
      SAVE_PASSWORD,
      SW_JS, LOGIN, LOGOUT,
      RESET_PASSWORD,
      CHANGE_PASSWORD + WILD_CARD
  };
}
