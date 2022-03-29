package com.art.service;

import com.art.config.exception.ApiException;
import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.*;
import com.art.model.supporting.enums.MoneyState;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.enums.TransactionType;
import com.art.model.supporting.filters.CashFilter;
import com.art.repository.MoneyRepository;
import com.art.repository.RentPaymentRepository;
import com.art.repository.SalePaymentRepository;
import com.art.specifications.MoneySpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoneyService {

  private static final String CASHING_COMMISSION = "Вывод_комиссия";
  private static final String CASHING = "Вывод";
  private static final String RESALE_SHARE = "Перепродажа доли";
  private static final String RE_BUY_SHARE = "Перепокупка доли";

  final MoneyRepository moneyRepository;
  final MoneySpecification specification;
  final TypeClosingService typeClosingService;
  final AfterCashingService afterCashingService;
  final UnderFacilityService underFacilityService;
  final FacilityService facilityService;
  final StatusService statusService;
  final NewCashDetailService newCashDetailService;
  final UserService userService;
  final TransactionLogService transactionLogService;
  final CashSourceService cashSourceService;
  final RentPaymentRepository rentPaymentRepository;
  final SalePaymentRepository salePaymentRepository;
  final AccountTransactionService accountTransactionService;

  public Money findById(Long id) {
    return moneyRepository.findById(id);
  }

  @Transactional
  public Money update(Money money) {
    return moneyRepository.saveAndFlush(money);
  }

  public Money createNew(Money money) {
    return update(money);
  }

  public void deleteById(Long id) {
    moneyRepository.deleteById(id);
  }

  public List<Money> findByRoomId(Long roomId) {
    return moneyRepository.findByRoomId(roomId);
  }

  @Transactional
  public Money create(Money money) {
    return createNew(money);
  }

  /**
   * Создать сумму на основе DTO
   *
   * @param moneyDTO DTO суммы
   * @return созданная сумма
   */
  public ApiResponse create(CreateMoneyDTO moneyDTO) {
    if (!moneyDTO.isCreateAccepted() && exist(moneyDTO)) {
      throw new ApiException("Такая сумма уже существует", HttpStatus.PRECONDITION_FAILED);
    }
    Money money = buildMoney(moneyDTO);
    money = create(money);
    transactionLogService.create(money, TransactionType.CREATE);
    return new ApiResponse(String.format("Деньги инвестора %s успешно добавлены", money.getInvestor().getLogin()));
  }

  private Facility findFacilityById(Long id) {
    return facilityService.findById(id);
  }

  private Money buildMoney(MoneyDTO dto) {
    Facility facility = findFacilityById(dto.getFacilityId());
    UnderFacility underFacility = underFacilityService.findById(dto.getUnderFacilityId());
    AppUser investor = userService.findById(dto.getInvestorId());
    CashSource cashSource = cashSourceService.findById(dto.getCashSourceId());
    NewCashDetail newCashDetail = newCashDetailService.findById(dto.getNewCashDetailId());
    ShareType shareType = ShareType.fromId(dto.getShareTypeId());

    return Money.builder()
        .facility(facility)
        .underFacility(underFacility)
        .investor(investor)
        .givenCash(dto.getCash())
        .dateGiven(dto.getDateGiven())
        .cashSource(cashSource)
        .newCashDetail(newCashDetail)
        .shareType(shareType)
        .build();
  }

  /**
   * Обновить деньги инвестора на основе DTO
   *
   * @param moneyDTO DTO для обновления
   * @return обновлённая сумма инвестора
   */
  public Money update(UpdateMoneyDTO moneyDTO) {
    Money dbMoney = findById(moneyDTO.getId());
    transactionLogService.update(dbMoney);

    Money money = buildMoney(moneyDTO);

    dbMoney.setFacility(money.getFacility());
    dbMoney.setUnderFacility(money.getUnderFacility());
    dbMoney.setInvestor(money.getInvestor());
    dbMoney.setCashSource(money.getCashSource());
    dbMoney.setNewCashDetail(money.getNewCashDetail());
    dbMoney.setShareType(money.getShareType());
    dbMoney.setGivenCash(money.getGivenCash());
    dbMoney.setDateGiven(money.getDateGiven());
    dbMoney.setRealDateGiven(moneyDTO.getRealDateGiven());
    return update(dbMoney);
  }

  public List<Money> findByIdIn(List<Long> idList) {
    return moneyRepository.findByIdIn(idList);
  }

  public List<Money> findBySource(String source) {
    return moneyRepository.findBySource(source);
  }

  public List<Money> findBySourceId(Long sourceId) {
    return moneyRepository.findBySourceId(sourceId);
  }

  public List<Money> findByInvestorId(Long investorId) {
    return moneyRepository.findByInvestorIdOrderByDateGivenAsc(investorId);
  }

  public Page<Money> findAll(CashFilter filters, Pageable pageable) {
    Page<Money> page = moneyRepository.findAll(specification.getFilter(filters), pageable);
    page.getContent().forEach(money -> Hibernate.initialize(money.getSourceUnderFacility()));
    return page;
  }

  /**
   * Вывести суммы инвесторов
   *
   * @param dto DTO для вывода
   * @return ответ об окончании операции
   */
  public ApiResponse cashingMoney(CashingMoneyDTO dto) {
    if (Objects.isNull(dto.getFacilityId())) {
      return ApiResponse.build422Response("Не задан id объекта");
    }
    List<Long> investorsIds = dto.getInvestorsIds();
    List<AppUser> investors = new ArrayList<>();
    investorsIds.forEach(id -> investors.add(userService.findById(id)));
    return cashing(investors, dto, dto.isAll());
  }

  /**
   * Вывести суммы инвесторов
   *
   * @param investors список инвесторов
   * @param dto       DTO для вывода
   * @param all       признак необходимости вывести все деньги
   * @return ответ об окончании операции
   */
  private ApiResponse cashing(List<AppUser> investors, CashingMoneyDTO dto, boolean all) {
    Facility facility = findFacilityById(dto.getFacilityId());
    TypeClosing typeClosing = typeClosingService.findByName(CASHING);
    TypeClosing typeClosingCommission = typeClosingService.findByName(CASHING_COMMISSION);
    UnderFacility underFacility = null;
    if (Objects.nonNull(dto.getUnderFacilityId())) {
      underFacility = underFacilityService.findById(dto.getUnderFacilityId());
    }
    for (AppUser investor : investors) {
      Money money = buildMoney(dto, facility, underFacility, investor);

      List<AfterCashing> cashingList = new ArrayList<>();
      Date dateClosing = money.getDateGiven();
      List<Money> monies = getMoneyForCashing(money);
      BigDecimal sumCash = monies.stream()
          .map(Money::getGivenCash)
          .reduce(BigDecimal.ZERO, BigDecimal::add); // все деньги инвестора

      BigDecimal commission = dto.getCommission(); // сумма комиссии
      BigDecimal commissionNoMore = dto.getCommissionNoMore(); // комиссия не более
      BigDecimal remainderSum; // сумма, которую надо вывести
      BigDecimal totalSum = BigDecimal.ZERO;

      if (all) {
        commission = getCommission(commission, commissionNoMore, sumCash);
        remainderSum = sumCash;
        money.setGivenCash(sumCash.subtract(commission));
      } else {
        commission = getCommission(commission, commissionNoMore, money.getGivenCash());
        remainderSum = totalSum;
        totalSum = money.getGivenCash().add(commission);
      }
      checkSums(sumCash, totalSum, commission);

      Money cashForManipulate = null;
      Money newCash = null;

      Money commissionCash = buildCommissionCash(commission, typeClosingCommission, money, dateClosing);

      StringBuilder sourceCash = new StringBuilder();
      int incr = 0;

      if (all) {
        cashingAll(typeClosing, money, cashingList, dateClosing, monies, commissionCash, sourceCash);
      } else {
        for (Money cash : monies) {
          if (remainderSum.compareTo(BigDecimal.ZERO) == 0) {
            break;
          }
          cashingList.add(new AfterCashing(cash.getId(), cash.getGivenCash()));
          sourceCash.append(cash.getId().toString()).append(incr == monies.size() - 1 ? "" : "|");
          // если сумма остатка, который надо вывести, больше текущей суммы инвестора
          if (isReminderSumGreaterThanInvestorSum(remainderSum, cash)) {
            // остаток = остаток - текущая сумма инвестора
            remainderSum = updateReminderSum(typeClosing, dateClosing, remainderSum, cash);
          } else {
            // если сумма выводимого остатка, меньше текущей суммы инвестора
            // создаём проводку, с которой сможем в дальнейшем проводить какие-либо действия
            // на сумму (текущие деньги вычитаем сумму остатка и комиссию)
            cashForManipulate = buildCashForManipulate(remainderSum, cash);
            // основную сумму блокируем для операций
            cash.setGivenCash(BigDecimal.ZERO);
            cash.setIsReinvest(1);
            cash.setIsDivide(1);
            // сохраняем сумму
            update(cash);

            // создаём новую сумму на остаток + комиссия
            newCash = buildNewCash(cash, remainderSum, dateClosing, typeClosing);

            remainderSum = BigDecimal.ZERO;
            fillCash(commissionCash, cash);
            fillCash(money, cash);
          }
          incr++;
        }
      }

      createMoney(typeClosing, money, commissionCash, sourceCash);
      cashingList.forEach(afterCashingService::create);
      createCommission(commissionCash, sourceCash);
      createCash(cashForManipulate, sourceCash);
      createCash(newCash, sourceCash);
    }
    return new ApiResponse("Вывод денег прошёл успешно");
  }

  private Money buildMoney(CashingMoneyDTO dto, Facility facility, UnderFacility underFacility, AppUser investor) {
    Money money = Money.builder()
        .investor(investor)
        .facility(facility)
        .underFacility(underFacility)
        .dateGiven(dto.getDateCashing())
        .givenCash(dto.getCash())
        .build();
    return money;
  }

  private void createCommission(Money commissionCash, StringBuilder sourceCash) {
    commissionCash.setSource(sourceCash.toString());
    create(commissionCash);
  }

  private void createMoney(TypeClosing typeClosing, Money money, Money commissionCash, StringBuilder sourceCash) {
    money.setGivenCash(money.getGivenCash().negate());
    money.setDateClosing(commissionCash.getDateClosing());
    money.setTypeClosing(typeClosing);
    money.setSource(sourceCash.toString());
    create(money);
  }

  private boolean isReminderSumGreaterThanInvestorSum(BigDecimal remainderSum, Money cash) {
    return cash.getGivenCash().subtract(remainderSum).compareTo(BigDecimal.ZERO) < 0;
  }

  private Money buildCashForManipulate(BigDecimal remainderSum, Money cash) {
    Money cashForManipulate = new Money(cash);
    cashForManipulate.setGivenCash(cash.getGivenCash().subtract(remainderSum));
    return cashForManipulate;
  }

  private BigDecimal updateReminderSum(TypeClosing typeClosing, Date dateClosing, BigDecimal remainderSum, Money cash) {
    remainderSum = remainderSum.subtract(cash.getGivenCash());
    updateSellerMoney(cash, dateClosing, typeClosing);
    return remainderSum;
  }

  private void createCash(Money cashForManipulate, StringBuilder sourceCash) {
    if (Objects.nonNull(cashForManipulate)) {
      cashForManipulate.setSource(sourceCash.toString());
      create(cashForManipulate);
    }
  }

  private Money buildCommissionCash(BigDecimal commission, TypeClosing typeClosingCommission, Money money, Date dateClosing) {
    return Money.builder()
        .givenCash(commission.negate())
        .typeClosing(typeClosingCommission)
        .investor(money.getInvestor())
        .facility(money.getFacility())
        .underFacility(money.getUnderFacility())
        .dateClosing(dateClosing)
        .build();
  }

  private Money buildNewCash(Money cash, BigDecimal remainderSum, Date dateClosing, TypeClosing typeClosing) {
    Money newCash = new Money(cash);
    newCash.setGivenCash(remainderSum);
    newCash.setDateClosing(dateClosing);
    newCash.setTypeClosing(typeClosing);
    return newCash;
  }

  private void cashingAll(TypeClosing typeClosing, Money money, List<AfterCashing> cashingList, Date dateClosing,
                          List<Money> monies, Money commissionCash, StringBuilder sourceCash) {
    int incr = 0;
    for (Money cash : monies) {
      cashingList.add(new AfterCashing(cash.getId(), cash.getGivenCash()));
      cash.setTypeClosing(typeClosing);
      cash.setDateClosing(dateClosing);
      sourceCash.append(cash.getId().toString()).append(incr == monies.size() - 1 ? "" : "|");

      fillCash(commissionCash, cash);
      fillCash(money, cash);

      update(cash);
      incr++;
    }
  }

  private void checkSums(BigDecimal sumCash, BigDecimal totalSum, BigDecimal commission) {
    if ((sumCash.compareTo(totalSum)) < 0) {
      String cashNoMore = String.valueOf(sumCash.subtract(commission).setScale(2, RoundingMode.DOWN));
      throw new ApiException("Сумма должна быть не более " + cashNoMore, HttpStatus.PRECONDITION_FAILED);
    }
  }

  private BigDecimal getCommission(BigDecimal commission, BigDecimal commissionNoMore, BigDecimal cash) {
    commission = (cash.multiply(commission)).divide(new BigDecimal(100), RoundingMode.CEILING);
    if (commissionNoMore != null && commission.compareTo(commissionNoMore) > 0) {
      commission = commissionNoMore;
    }
    return commission;
  }

  private void fillCash(Money to, Money from) {
    to.setDateGiven(from.getDateGiven());
    to.setCashSource(from.getCashSource());
    to.setNewCashDetail(from.getNewCashDetail());
    to.setShareType(from.getShareType());
    to.setDateReport(from.getDateReport());
    to.setSourceFacility(from.getSourceFacility());
    to.setSourceUnderFacility(from.getSourceUnderFacility());
    to.setSourceFlowsId(from.getSourceFlowsId());
    to.setRoom(from.getRoom());
  }

  private List<Money> getMoneyForCashing(Money cashForGetting) {
    CashFilter filter = new CashFilter();
    filter.setInvestor(cashForGetting.getInvestor());
    filter.setFacility(cashForGetting.getFacility().getName());
    if (Objects.nonNull(cashForGetting.getUnderFacility())) {
      filter.setUnderFacility(cashForGetting.getUnderFacility().getName());
    }
    Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
    List<Money> monies = moneyRepository.findAll(specification.getFilterForCashing(filter), pageable).getContent();
    if (monies.isEmpty()) {
      throw new ApiException("Нет денег для вывода", HttpStatus.PRECONDITION_FAILED);
    }
    return monies;
  }

  /**
   * Разделение денег
   *
   * @param dividedCashDTO DTO для деления
   * @return ответ
   */
  public ApiResponse divideCash(DividedCashDTO dividedCashDTO) {
    // Получаем список денег по идентификаторам
    List<Money> monies = findByIdIn(dividedCashDTO.getInvestorCashList());

    List<Long> remainingUnderFacilityList = dividedCashDTO.getExcludedUnderFacilitiesIdList();

    // Получаем подобъект, куда надо разделить сумму
    UnderFacility underFacility = underFacilityService.findById(dividedCashDTO.getReUnderFacilityId());

    // Получаем объект, в который надо разделить сумму
    Facility facility = findFacilityById(underFacility.getFacility().getId());

    // Получаем список подобъектов объекта
    List<UnderFacility> underFacilityList = underFacilityService.findByFacilityId(facility.getId());

    List<Room> rooms = new ArrayList<>(0);

    // Если в списке подобъектов присутствует подобъект, из которого должен состоять остаток суммы, заносим помещения
    // этого подобъекта в список
    underFacilityList.forEach(uf -> remainingUnderFacilityList.forEach(ruf -> {
      if (uf.getId().equals(ruf)) {
        rooms.addAll(uf.getRooms());
      }
    }));

    // Вычисляем стоимость объекта, складывая стоимости помещений, из которых должен состоять остаток
    BigDecimal coastFacility = rooms
        .stream()
        .map(Room::getCost)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.CEILING);

    // Вычисляем стоимость подобъекта, куда надо разделить сумму
    BigDecimal coastUnderFacility = underFacility.getRooms().stream()
        .map(Room::getCost)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.CEILING);

    // Вычисляем % для выделения доли
    BigDecimal divided = coastUnderFacility.divide(coastFacility, 20, RoundingMode.CEILING);
    monies = monies.stream()
        .filter(f -> Objects.nonNull(f.getGivenCash()))
        .collect(Collectors.toList());

    int sumsCnt = monies.size();
    sendStatus("Начинаем разделять суммы");
    int counter = 0;
    Set<Money> newMonies = new HashSet<>();

    for (Money f : monies) {
      counter++;
      sendStatus(String.format("Разделяем %d из %d сумм", counter, sumsCnt));
      BigDecimal invCash = f.getGivenCash();
      BigDecimal sumInUnderFacility = divided.multiply(invCash);
      BigDecimal sumRemainder = invCash.subtract(sumInUnderFacility);
      f.setIsDivide(1);

      Money cash = buildMoney(underFacility, f, sumInUnderFacility);

      f.setGivenCash(sumRemainder);
      if (f.getGivenCash().signum() == 0) {
        f.setIsDivide(1);
        f.setIsReinvest(1);
        update(f);
      } else {
        create(f);
      }
      create(cash);
      newMonies.add(cash);
      newMonies.add(f);
    }
    transactionLogService.createDivideCashLog(newMonies);
    return new ApiResponse("Разделение сумм прошло успешно");
  }

  private Money buildMoney(UnderFacility underFacility, Money money, BigDecimal sumInUnderFacility) {
    return Money.builder()
        .source(money.getId().toString())
        .givenCash(sumInUnderFacility)
        .dateGiven(money.getDateGiven())
        .facility(money.getFacility())
        .investor(money.getInvestor())
        .cashSource(money.getCashSource())
        .newCashDetail(money.getNewCashDetail())
        .underFacility(underFacility)
        .dateClosing(null)
        .typeClosing(null)
        .shareType(money.getShareType())
        .dateReport(money.getDateReport())
        .sourceFacility(money.getSourceFacility())
        .sourceUnderFacility(money.getSourceUnderFacility())
        .room(money.getRoom())
        .realDateGiven(money.getRealDateGiven())
        .build();
  }

  /**
   * Закрыть суммы по инвесторам (массовое)
   *
   * @param closeCashDTO DTO для закрытия сумм
   * @return сообщение
   */
  public ApiResponse close(CloseCashDTO closeCashDTO) {
    List<Money> cashList = new ArrayList<>(0);
    closeCashDTO.getInvestorCashIdList().forEach(id -> cashList.add(findById(id)));

    Date dateClose = closeCashDTO.getDateReinvest();
    Date realDateGiven = closeCashDTO.getRealDateGiven();
    // список сумм, которые закрываем для вывода
    Set<Money> closeCashes = new HashSet<>();
    // список сумм, которые закрываем для перепродажи доли
    Set<Money> resaleCashes = new HashSet<>();
    // список сумм, которые получатся на выходе
    Set<Money> newCashes = new HashSet<>();

    for (Money money : cashList) {
      if (Objects.nonNull(closeCashDTO.getInvestorBuyerId())) { // Перепродажа доли
        resaleShare(closeCashDTO.getInvestorBuyerId(), dateClose, realDateGiven, resaleCashes, newCashes, money);
      } else {
        cashingMoney(dateClose, realDateGiven, closeCashes, money);
      }
    }
    if (!closeCashes.isEmpty()) {
      transactionLogService.close(closeCashes);
    } else {
      transactionLogService.resale(resaleCashes, newCashes);
    }
    return new ApiResponse("Массовое закрытие прошло успешно.");
  }

  private void cashingMoney(Date dateClose, Date realDateGiven, Set<Money> closeCashes, Money money) {
    TypeClosing cashing = typeClosingService.findByName(CASHING);
    Money cashForTx = new Money(money);
    cashForTx.setId(money.getId());
    money.setDateClosing(dateClose);
    money.setTypeClosing(cashing);
    money.setRealDateGiven(realDateGiven);
    update(money);
    closeCashes.add(cashForTx);
  }

  private void resaleShare(Long buyerId, Date dateClose, Date realDateGiven, Set<Money> resaleCashes,
                           Set<Money> newCashes, Money sellerMoney) {
    AppUser buyer = userService.findById(buyerId);
    TypeClosing closingInvest = typeClosingService.findByName(RESALE_SHARE);

    Money buyerMoney = createBuyerMoney(dateClose, realDateGiven, sellerMoney, buyer);
    Money negativeMoney = createNegativeMoney(dateClose, sellerMoney, closingInvest);

    updateSellerMoney(sellerMoney, dateClose, closingInvest);

    resaleCashes.add(sellerMoney);
    newCashes.add(sellerMoney);
    newCashes.add(buyerMoney);
    newCashes.add(negativeMoney);
  }

  private void updateSellerMoney(Money sellerMoney, Date dateClose, TypeClosing closingInvest) {
    sellerMoney.setDateClosing(dateClose);
    sellerMoney.setTypeClosing(closingInvest);
    update(sellerMoney);
  }

  private Money createNegativeMoney(Date dateClose, Money sellerMoney, TypeClosing closingInvest) {
    Money negativeMoney = new Money(sellerMoney);

    negativeMoney.setCashSource(null);
    negativeMoney.setGivenCash(negativeMoney.getGivenCash().negate());
    negativeMoney.setSourceId(sellerMoney.getId());
    negativeMoney.setSource(null);
    negativeMoney.setDateGiven(dateClose);
    negativeMoney.setDateClosing(dateClose);
    negativeMoney.setTypeClosing(closingInvest);

    createNew(negativeMoney);
    return negativeMoney;
  }

  private Money createBuyerMoney(Date dateClose, Date realDateGiven, Money sellerMoney, AppUser buyer) {
    NewCashDetail newCashDetail = newCashDetailService.findByName(RE_BUY_SHARE);
    Money buyerMoney = new Money(sellerMoney);

    buyerMoney.setInvestor(buyer);
    buyerMoney.setDateGiven(dateClose);
    buyerMoney.setSourceId(sellerMoney.getId());
    buyerMoney.setCashSource(null);
    buyerMoney.setSource(null);
    buyerMoney.setNewCashDetail(newCashDetail);
    buyerMoney.setRealDateGiven(realDateGiven);

    buyerMoney = createNew(buyerMoney);
    return buyerMoney;
  }

  public ApiResponse resaleSimple(ResaleMoneyDTO moneyDTO) {
    AppUser buyer = userService.findById(moneyDTO.getBuyerId());
    TypeClosing typeClosing = typeClosingService.findByName(RESALE_SHARE);

    Money sellerMoney = findById(moneyDTO.getId());

    Money buyerMoney = createBuyerMoney(moneyDTO.getDateClose(), moneyDTO.getRealDateGiven(), sellerMoney, buyer);
    Money negativeMoney = createNegativeMoney(moneyDTO.getDateClose(), sellerMoney, typeClosing);

    updateSellerMoney(sellerMoney, moneyDTO.getDateClose(), typeClosing);

    Money transactionOldCash = new Money(sellerMoney);
    transactionOldCash.setId(sellerMoney.getId());

    Set<Money> cashSet = new HashSet<>();
    cashSet.add(buyerMoney);
    cashSet.add(negativeMoney);
    cashSet.add(transactionOldCash);
    transactionLogService.resale(Collections.singleton(transactionOldCash), cashSet);
    return ApiResponse.build200Response(
        String.format("Перепродажа доли инвестора %s прошла успешно",
            sellerMoney.getInvestor().getLogin())
    );
  }

  private void sendStatus(String message) {
    statusService.sendStatus(message);
  }

  /**
   * Проверить, если такая же сумма уже присутствует на сервере
   *
   * @param dto DTO суммы
   * @return результат проверки
   */
  private boolean exist(MoneyDTO dto) {
    Date dateGiven = dto.getDateGiven();
    List<Money> monies = moneyRepository.findDuplicate(dto.getInvestorId(), dto.getCash(),
        dto.getFacilityId(), dateGiven);
    return monies != null && !monies.isEmpty();
  }

  /**
   * Удалить список денег инвесторов
   *
   * @param dto DTO для удаления
   * @return ответ
   */
  public ApiResponse deleteList(DeleteMoneyDTO dto) {
    ApiResponse response = new ApiResponse();
    List<Money> listToDelete = findByIdIn(dto.getMoneyIds());
    List<AfterCashing> afterCashingList = afterCashingService.findAll();
    int counter = 0;
    AccountTxDTO accountTxDTO = new AccountTxDTO();

    for (Money deleting : listToDelete) {
      AccountTransaction transaction = deleting.getTransaction();
      if (Objects.nonNull(transaction)) {
        accountTxDTO.addTxId(transaction.getId());
        accountTxDTO.addCashTypeId(transaction.getCashType());
        transaction.removeMoney(deleting);
      }
      counter++;
      sendStatus(String.format("Удаляем %d из %d сумм", counter, listToDelete.size()));

      updatePaymentsIfNeeded(deleting);

      deleteBySourceIfNeeded(afterCashingList, deleting);

      findBySourceId(deleting.getId()).forEach(this::updateOrDeleteCash);

      deleteBySourceIdIfNeeded(deleting);
      deleteById(deleting.getId());
      response.setMessage("Данные успешно удалены");
    }

    if (!accountTxDTO.getTxIds().isEmpty()) {
      accountTransactionService.delete(accountTxDTO);
    }
    sendStatus("OK");
    return response;
  }

  private void updateOrDeleteCash(Money cash) {
    if (cash.getGivenCash().signum() == -1) {
      deleteById(cash.getId());
    } else {
      cash.setSourceId(null);
      update(cash);
    }
  }

  private void deleteBySourceIdIfNeeded(Money deleting) {
    if (Objects.nonNull(deleting.getSourceId())) {
      List<Money> monies = findBySourceId(deleting.getSourceId())
          .stream()
          .filter(ic -> !Objects.equals(deleting, ic))
          .collect(Collectors.toList());
      if (!monies.isEmpty()) {
        monies.forEach(investorsCash -> deleteById(investorsCash.getId()));
      }

      Money parentCash = findById(deleting.getSourceId());
      if (Objects.nonNull(parentCash)) {
        parentCash.setIsReinvest(0);
        parentCash.setIsDivide(0);
        parentCash.setTypeClosing(null);
        parentCash.setDateClosing(null);
        update(parentCash);
      }
    }
  }

  private void deleteBySourceIfNeeded(List<AfterCashing> afterCashingList, Money deleting) {
    if (Objects.nonNull(deleting.getSource())) {
      List<Long> sourceIdList = getSourceIds(deleting);
      sourceIdList.forEach(parentCashId -> {
        Money parentCash = findById(parentCashId);
        if (Objects.nonNull(parentCash)) {
          List<AfterCashing> afterCashing = afterCashingList.stream()
              .filter(ac -> ac.getOldId().equals(parentCashId))
              .collect(Collectors.toList());

          if (isCashing(deleting, afterCashing)) {

            List<Money> childCash = findBySource(deleting.getSource());
            AfterCashing cashToDel = afterCashing.stream()
                .filter(ac -> ac.getOldId().equals(parentCashId))
                .findFirst()
                .orElse(afterCashing.get(0));

            parentCash.setGivenCash(cashToDel.getOldValue());
            childCash.forEach(cbs -> deleteById(cbs.getId()));
            afterCashingService.deleteById(cashToDel.getId());
          }
          Money makeDelete = findBySource(parentCash.getId().toString())
              .stream()
              .filter(m -> !m.getId().equals(deleting.getId()))
              .findFirst().orElse(null);

          updateParentCashIfNeeded(parentCash, makeDelete);

          if (isSameMonies(deleting, parentCash)) {
            parentCash.setGivenCash(parentCash.getGivenCash().add(deleting.getGivenCash()));
          }

          findBySourceId(parentCashId).stream()
              .filter(oc -> !deleting.getId().equals(oc.getId()))
              .forEach(oCash -> {
                parentCash.setGivenCash(parentCash.getGivenCash().add(oCash.getGivenCash()));
                deleteById(oCash.getId());
              });
          update(parentCash);
        }
      });
    }
  }

  private void updateParentCashIfNeeded(Money parentCash, Money makeDelete) {
    if (Objects.isNull(makeDelete)) {
      parentCash.setIsReinvest(0);
      parentCash.setIsDivide(0);
      parentCash.setTypeClosing(null);
      parentCash.setDateClosing(null);
    }
  }

  private boolean isCashing(Money deleting, List<AfterCashing> afterCashing) {
    return !afterCashing.isEmpty() && Objects.nonNull(deleting.getTypeClosing()) &&
        (deleting.getTypeClosing().getName().equalsIgnoreCase(CASHING)
            || deleting.getTypeClosing().getName().equalsIgnoreCase(CASHING_COMMISSION));
  }

  private List<Long> getSourceIds(Money deleting) {
    String[] tmp = deleting.getSource().split(Pattern.quote("|"));
    List<Long> sourceIdList = new ArrayList<>(tmp.length);
    if (tmp.length > 0 && !tmp[tmp.length - 1].equals("")) {
      for (String bigInt : tmp) {
        sourceIdList.add(Long.valueOf(bigInt));
      }
    }
    return sourceIdList;
  }

  private boolean isSameMonies(Money deleting, Money parentCash) {
    return deleting.getFacility().equals(parentCash.getFacility()) &&
        deleting.getInvestor().equals(parentCash.getInvestor()) &&
        deleting.getShareType().equals(parentCash.getShareType()) &&
        deleting.getTypeClosing() == null &&
        deleting.getDateGiven().compareTo(parentCash.getDateGiven()) == 0;
  }

  private void updatePaymentsIfNeeded(Money deleting) {
    if (Objects.nonNull(deleting.getSourceFlowsId())) {
      getSourceIdList(deleting.getSourceFlowsId())
          .forEach(id -> {
            updateRentPayment(id);
            updateSalePayment(id);
          });
    }
  }

  private List<Long> getSourceIdList(String field) {
    return Stream.of(field.split(Pattern.quote("|")))
        .map(Long::valueOf)
        .collect(Collectors.toList());
  }

  private void updateRentPayment(Long id) {
    RentPayment rentPayment = rentPaymentRepository.findOne(id);
    if (Objects.nonNull(rentPayment)) {
      rentPayment.setIsReinvest(0);
      rentPaymentRepository.saveAndFlush(rentPayment);
    }
  }

  private void updateSalePayment(Long id) {
    SalePayment salePayment = salePaymentRepository.findOne(id);
    if (Objects.nonNull(salePayment)) {
      salePayment.setIsReinvest(0);
      salePaymentRepository.saveAndFlush(salePayment);
    }
  }

  /**
   * Согласовать суммы
   *
   * @param dto суммы для согласования
   * @return ответ
   */
  public ApiResponse accept(AcceptMoneyDTO dto) {
    dto.getAcceptedMoneyIds()
        .forEach(id -> {
          Money money = findById(id);
          if (Objects.nonNull(money) && MoneyState.ACTIVE != money.getState()) {
            money.setState(MoneyState.ACTIVE);
            update(money);
          }
        });
    return new ApiResponse("Суммы успешно согласованы");
  }

  /**
   * Получить список открытых сумм в конкретном проекте у конкретного инвестора
   *
   * @param dto DTO для перепродажи
   * @return список открытых сумм
   */
  public List<InvestorCashDTO> getOpenedMonies(ReBuyShareDTO dto) {
    List<Money> monies = moneyRepository.getOpenedMoniesByFacility(dto.getFacilityId(), dto.getSellerId());
    return monies
        .stream()
        .map(InvestorCashDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Перекупить долю в объекте
   *
   * @param dto DTO для перекупки
   * @return ответ о выполнении
   */
  public ApiResponse reBuyShare(ReBuyShareDTO dto) {
    checkDTO(dto);
    AppUser investor = userService.findById(dto.getBuyerId());
    if (Objects.isNull(investor)) {
      throw new ApiException("Инвестор не найден", HttpStatus.NOT_FOUND);
    }
    List<Money> sellerMonies = closeSellerMonies(dto);
    List<Money> buyerMonies = openBuyerMonies(sellerMonies, investor);
    AccountTransaction transaction = accountTransactionService.reBuy(dto, buyerMonies);
    accountTransactionService.resale(dto, sellerMonies, transaction);
    return new ApiResponse("Перепродажа доли прошла успешно", HttpStatus.OK.value());
  }

  /**
   * Закрыть деньги инвестора продавца
   *
   * @param dto DTO для закрытия
   * @return список закрытых сумм
   */
  private List<Money> closeSellerMonies(ReBuyShareDTO dto) {
    TypeClosing typeClosing = typeClosingService.findByName(RESALE_SHARE);
    List<Long> openedCash = dto.getOpenedCash()
        .stream()
        .map(InvestorCashDTO::getId)
        .collect(Collectors.toList());
    List<Money> sellerMonies = new ArrayList<>(moneyRepository.findByIdIn(openedCash));
    sellerMonies.forEach(money -> {
      money.setTypeClosing(typeClosing);
      money.setDateClosing(dto.getRealDateGiven());
    });
    return moneyRepository.save(sellerMonies);
  }

  /**
   * Создать суммы инвестору покупателю в деньгах инвесторов
   *
   * @param sellerMonies закрытые суммы инвестора продавца
   * @param investor     инвестор покупатель
   */
  private List<Money> openBuyerMonies(List<Money> sellerMonies, AppUser investor) {
    NewCashDetail newCashDetail = newCashDetailService.findByName(RE_BUY_SHARE);
    if (Objects.isNull(newCashDetail)) {
      throw new ApiException("Не найдены детали новых денег \"Перепокупка доли\"", HttpStatus.NOT_FOUND);
    }
    List<Money> buyerMonies = new ArrayList<>();
    sellerMonies.forEach(money -> {
      Money cash = new Money(money);
      cash.setInvestor(investor);
      cash.setTypeClosing(null);
      cash.setDateClosing(null);
      cash.setSourceFacility(null);
      cash.setSourceUnderFacility(null);
      cash.setSourceFlowsId(null);
      cash.setIsReinvest(0);
      cash.setSourceId(null);
      cash.setSource(null);
      cash.setIsDivide(0);
      cash.setRealDateGiven(money.getDateClosing());
      cash.setTransaction(null);
      cash.setNewCashDetail(newCashDetail);
      buyerMonies.add(cash);
    });
    return moneyRepository.save(buyerMonies);
  }

  /**
   * Проверить DTO
   *
   * @param dto DTO для проверки
   */
  private void checkDTO(ReBuyShareDTO dto) {
    if (dto.getOpenedCash().isEmpty()) {
      throw new ApiException("Не указаны суммы для перепродажи", HttpStatus.PRECONDITION_FAILED);
    }
    if (Objects.isNull(dto.getBuyerId())) {
      throw new ApiException("Не указан покупатель доли", HttpStatus.PRECONDITION_FAILED);
    }
    if (Objects.isNull(dto.getRealDateGiven())) {
      throw new ApiException("Не указана дата реальной передачи денег", HttpStatus.PRECONDITION_FAILED);
    }
    checkSums(dto);
  }

  /**
   * Проверить суммы
   *
   * @param dto DTO для проверки
   */
  private void checkSums(ReBuyShareDTO dto) {
    BigDecimal sellerSum = dto.getOpenedCash()
        .stream()
        .map(InvestorCashDTO::getGivenCash)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal buyerSum = accountTransactionService.getInvestorBalance(dto.getBuyerId());
    buyerSum = buyerSum.add(new BigDecimal("0.50"));
    if (sellerSum.compareTo(buyerSum) > 0) {
      throw new ApiException("Недостаточно денег для перепокупки доли", HttpStatus.PRECONDITION_FAILED);
    }
  }

}
