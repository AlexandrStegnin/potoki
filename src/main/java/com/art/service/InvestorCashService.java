package com.art.service;

import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.dto.CloseCashDTO;
import com.art.model.supporting.dto.DividedCashDTO;
import com.art.model.supporting.dto.ReinvestCashDTO;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.filters.CashFilter;
import com.art.repository.InvestorCashRepository;
import com.art.specifications.InvestorCashSpecification;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvestorCashService {

    private static final String RESALE_SHARE = "Перепродажа доли";
    private final InvestorCashRepository investorCashRepository;
    private final InvestorCashSpecification specification;
    private final TypeClosingService typeClosingService;
    private final AfterCashingService afterCashingService;
    private final UnderFacilityService underFacilityService;
    private final FacilityService facilityService;
    private final StatusService statusService;
    private final NewCashDetailService newCashDetailService;
    private final UserService userService;
    private final TransactionLogService transactionLogService;

    @Autowired
    public InvestorCashService(InvestorCashRepository investorCashRepository, InvestorCashSpecification specification,
                               TypeClosingService typeClosingService, AfterCashingService afterCashingService,
                               UnderFacilityService underFacilityService, FacilityService facilityService,
                               StatusService statusService, NewCashDetailService newCashDetailService, UserService userService,
                               TransactionLogService transactionLogService) {
        this.investorCashRepository = investorCashRepository;
        this.specification = specification;
        this.typeClosingService = typeClosingService;
        this.afterCashingService = afterCashingService;
        this.underFacilityService = underFacilityService;
        this.facilityService = facilityService;
        this.statusService = statusService;
        this.newCashDetailService = newCashDetailService;
        this.userService = userService;
        this.transactionLogService = transactionLogService;
    }

    public List<InvestorCash> findAll() {
        return investorCashRepository.findAll();
    }

    public InvestorCash findById(Long id) {
        return investorCashRepository.findById(id);
    }

    public List<InvestorCash> findByFacilityId(Long facilityId) {
        return investorCashRepository.findByFacilityId(facilityId);
    }

    public InvestorCash update(InvestorCash investorCash) {
        investorCash = investorCashRepository.saveAndFlush(investorCash);
        return investorCash;
    }

    public InvestorCash createNew(InvestorCash investorCash) {
        investorCash = investorCashRepository.saveAndFlush(investorCash);
        return investorCash;
    }

    public void deleteById(Long id) {
        investorCashRepository.deleteById(id);
    }

    public void delete(InvestorCash cash) {
        investorCashRepository.delete(cash);
    }

    public void saveAll(List<InvestorCash> investorCashes) {
        investorCashRepository.save(investorCashes);
    }

    public List<InvestorCash> findByRoomId(Long roomId) {
        return investorCashRepository.findByRoomId(roomId);
    }

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public InvestorCash create(InvestorCash investorCash) {
        return this.em.merge(investorCash);
    }

    public List<InvestorCash> findByIdIn(List<Long> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorCash> investorsCashCriteriaQuery = cb.createQuery(InvestorCash.class);
        Root<InvestorCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorCash.class);
        investorsCashRoot.fetch(InvestorCash_.typeClosing, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(investorsCashRoot.get(InvestorCash_.id).in(idList));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorCash> findBySource(String source) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorCash> investorsCashCriteriaQuery = cb.createQuery(InvestorCash.class);
        Root<InvestorCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorCash.class);
        investorsCashRoot.fetch(InvestorCash_.typeClosing, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorCash_.source), source));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorCash> findBySourceId(Long sourceId) {
        return investorCashRepository.findBySourceId(sourceId);
    }

    public List<InvestorCash> findByInvestorId(Long investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorCash> investorsCashCriteriaQuery = cb.createQuery(InvestorCash.class);
        Root<InvestorCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorCash.class);
        investorsCashRoot.fetch(InvestorCash_.underFacility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorCash_.investor).get(AppUser_.id), investorId));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorCash_.dateGiven)));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorCash> findAllWithAllFields() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<InvestorCash> investorsCashCriteriaQuery = cb.createQuery(InvestorCash.class);
        Root<InvestorCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorCash.class);
        investorsCashRoot.fetch(InvestorCash_.facility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.isNotNull(investorsCashRoot.get(InvestorCash_.facility)));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorCash_.dateGiven)));
        List<InvestorCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        cash.forEach(c -> {
            Hibernate.initialize(c.getInvestor());
            Hibernate.initialize(c.getFacility());
            Hibernate.initialize(c.getUnderFacility());
            Hibernate.initialize(c.getRoom());
        });

        return cash;
    }

    public Page<InvestorCash> findAll(CashFilter filters, Pageable pageable) {
        return investorCashRepository.findAll(
                specification.getFilter(filters),
                pageable
        );
    }

    public String cashingAllMoney(final SearchSummary searchSummary) {
        return cashing(searchSummary, true);
    }

    public String cashingMoney(final SearchSummary searchSummary) {
        return cashing(searchSummary, false);
    }

    public String cashing(SearchSummary searchSummary, boolean all) {
        UnderFacility underFacility = null;
        if (searchSummary.getUnderFacility() != null) {
            if (searchSummary.getUnderFacility().getId() != null) {
                underFacility = underFacilityService.findById(searchSummary.getUnderFacility().getId());
            } else if (searchSummary.getUnderFacility().getName() != null) {
                underFacility = underFacilityService.findByName(searchSummary.getUnderFacility().getName());
            }
            searchSummary.setUnderFacility(underFacility);
        }
        final String[] result = {""};
        if (searchSummary.getInvestorsList() != null) {
            UnderFacility finalUnderFacility = underFacility;
            searchSummary.getInvestorsList().forEach(user -> {
                if (searchSummary.getInvestorCash() != null) {
                    List<UnderFacility> underFacilities = searchSummary.getUnderFacilityList();
                    InvestorCash invCash = searchSummary.getInvestorCash();
                    invCash.setInvestor(user);
                    invCash.setUnderFacility(finalUnderFacility);
                    List<AfterCashing> cashingList = new ArrayList<>(0);
                    final InvestorCash[] cashForGetting = {searchSummary.getInvestorCash()};
                    if (cashForGetting[0].getUnderFacility() != null && cashForGetting[0].getUnderFacility().getId() == null) {
                        cashForGetting[0].setUnderFacility(null);
                    }
                    Date dateClosingInvest = cashForGetting[0].getDateGiven();
                    List<InvestorCash> investorCashes = getMoneyForCashing(cashForGetting[0]);
                    if (investorCashes.size() == 0) {
                        result[0] = "Нет денег для вывода";
                        return;
                    }
                    final BigDecimal sumCash = investorCashes.stream().map(InvestorCash::getGivenCash).reduce(BigDecimal.ZERO, BigDecimal::add); // все деньги инвестора
                    BigDecimal commission = searchSummary.getCommission(); // сумма комиссии
                    final BigDecimal commissionNoMore = searchSummary.getCommissionNoMore(); // комиссия не более
                    final BigDecimal[] remainderSum = new BigDecimal[1]; // сумма, которую надо вывести
                    BigDecimal totalSum = new BigDecimal(BigInteger.ZERO);
                    remainderSum[0] = totalSum;
                    if (all) {
                        commission = (sumCash.multiply(commission)).divide(new BigDecimal(100), BigDecimal.ROUND_CEILING);
                        if (commissionNoMore != null && commission.compareTo(commissionNoMore) > 0) {
                            commission = commissionNoMore;
                        }
                        remainderSum[0] = sumCash;
                        cashForGetting[0].setGivenCash(sumCash.subtract(commission));
                    } else {
                        commission = (cashForGetting[0].getGivenCash().multiply(commission)).divide(new BigDecimal(100), BigDecimal.ROUND_CEILING);
                        if (commissionNoMore != null && commission.compareTo(commissionNoMore) > 0) {
                            commission = commissionNoMore;
                        }
                        totalSum = cashForGetting[0].getGivenCash().add(commission);
                        remainderSum[0] = totalSum;
                    }
                    if ((sumCash.compareTo(totalSum)) < 0) {
                        result[0] = "Сумма должна быть не более " + String.valueOf(sumCash).substring(0, String.valueOf(sumCash).indexOf("."));
                        return;
                    }

                    final TypeClosing typeClosing = typeClosingService.findByName("Вывод");
                    final TypeClosing typeClosingCommission = typeClosingService.findByName("Вывод_комиссия");

                    final InvestorCash[] commissionCash = {new InvestorCash()};
                    final InvestorCash[] cashForManipulate = {null};
                    commissionCash[0].setGivenCash(commission.negate());
                    commissionCash[0].setTypeClosing(typeClosingCommission);
                    commissionCash[0].setInvestor(cashForGetting[0].getInvestor());
                    commissionCash[0].setFacility(cashForGetting[0].getFacility());
                    commissionCash[0].setUnderFacility(cashForGetting[0].getUnderFacility());
                    commissionCash[0].setDateClosing(dateClosingInvest);

                    StringBuilder sourceCash = new StringBuilder();
                    final AtomicInteger[] incr = {new AtomicInteger()};
                    final InvestorCash[] newCash = {null};

                    if (all) {
                        investorCashes.forEach(ic -> {
                            cashingList.add(new AfterCashing(ic.getId(), ic.getGivenCash()));
                            ic.setTypeClosing(typeClosing);
                            ic.setDateClosing(dateClosingInvest);
                            if (incr[0].get() == investorCashes.size() - 1) {
                                sourceCash.append(ic.getId().toString());
                            } else {
                                sourceCash.append(ic.getId().toString()).append("|");
                            }

                            fillCash(commissionCash[0], ic);
                            fillCash(cashForGetting[0], ic);

                            update(ic);
                            incr[0].getAndIncrement();
                        });
                    } else {

                        investorCashes.forEach(ic -> {
                            if (remainderSum[0].equals(BigDecimal.ZERO)) return;
                            cashingList.add(new AfterCashing(ic.getId(), ic.getGivenCash()));
                            if (incr[0].get() == investorCashes.size() - 1) {
                                sourceCash.append(ic.getId().toString());
                            } else {
                                sourceCash.append(ic.getId().toString()).append("|");
                            }
                            // если сумма остатка, который надо вывести, больше текущей суммы инвестора
                            if (ic.getGivenCash().subtract(remainderSum[0]).compareTo(BigDecimal.ZERO) < 0) {
                                // остаток = остаток - текущая сумма инвестора
                                remainderSum[0] = remainderSum[0].subtract(ic.getGivenCash());
                                ic.setDateClosing(dateClosingInvest);
                                ic.setTypeClosing(typeClosing);
                                update(ic);
                            } else {
                                // иначе если сумма остатка, который надо вывести, меньше текущей суммы инвестора
                                // создаём проводку, с которой сможем в дальнейшем проводить какие-либо действия
                                // на сумму (текущие деньги вычитаем сумму остатка и комиссию)
                                cashForManipulate[0] = new InvestorCash(ic);
                                cashForManipulate[0].setGivenCash(ic.getGivenCash().subtract(remainderSum[0]));
                                // основную сумму блокируем для операций
                                ic.setGivenCash(BigDecimal.ZERO);
                                ic.setIsReinvest(1);
                                ic.setIsDivide(1);
                                // сохраняем сумму
                                update(ic);

                                // создаём новую сумму на остаток + комиссия
                                newCash[0] = new InvestorCash(ic);
                                newCash[0].setGivenCash(remainderSum[0]);
                                newCash[0].setDateClosing(dateClosingInvest);
                                newCash[0].setTypeClosing(typeClosing);
                                remainderSum[0] = BigDecimal.ZERO;
                                fillCash(commissionCash[0], ic);
                                fillCash(cashForGetting[0], ic);
                            }
                            incr[0].getAndIncrement();
                        });
                    }

                    cashForGetting[0].setGivenCash(cashForGetting[0].getGivenCash().negate());
                    cashForGetting[0].setDateClosing(commissionCash[0].getDateClosing());
                    cashForGetting[0].setTypeClosing(typeClosing);

                    cashingList.forEach(afterCashingService::create);
                    cashForGetting[0].setSource(sourceCash.toString());
                    commissionCash[0].setSource(sourceCash.toString());
                    if (!Objects.equals(null, cashForManipulate[0])) {
                        cashForManipulate[0].setSource(sourceCash.toString());
                        create(cashForManipulate[0]);
                    }
                    if (newCash[0] != null) {
                        newCash[0].setSource(sourceCash.toString());
                        create(newCash[0]);
                    }
                    create(cashForGetting[0]);
                    create(commissionCash[0]);
                }
            });
        }
        return result[0];
    }

    private void fillCash(InvestorCash to, InvestorCash from) {
        to.setDateGiven(from.getDateGiven());
        to.setCashSource(from.getCashSource());
//        to.setCashType(from.getCashType());
        to.setNewCashDetail(from.getNewCashDetail());
//        to.setInvestorsType(from.getInvestorsType());
        to.setShareType(from.getShareType());
        to.setDateReport(from.getDateReport());
        to.setSourceFacility(from.getSourceFacility());
        to.setSourceUnderFacility(from.getSourceUnderFacility());
        to.setSourceFlowsId(from.getSourceFlowsId());
        to.setRoom(from.getRoom());
    }

    public List<InvestorCash> getMoneyForCashing(InvestorCash cashForGetting) {
        CashFilter filter = new CashFilter();
        filter.setInvestor(cashForGetting.getInvestor());
        filter.setFacility(cashForGetting.getFacility().getName());
        if (!Objects.equals(null, cashForGetting.getUnderFacility())) {
            filter.setUnderFacility(cashForGetting.getUnderFacility().getName());
        }
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
        return investorCashRepository.findAll(
                specification.getFilterForCashing(filter), pageable).getContent();

    }

    public List<InvestorCash> getInvestedMoney() {
        TypeClosing typeClosing = typeClosingService.findByName(RESALE_SHARE);
        return investorCashRepository.findAll(specification.getInvestedMoney(typeClosing.getId()));
    }

    /**
     * Разделение денег
     *
     * @param dividedCashDTO DTO для деления
     * @return ответ
     */
    public ApiResponse divideCash(DividedCashDTO dividedCashDTO) {
        // Получаем id сумм, которые надо разделить
        List<Long> idsList = dividedCashDTO.getInvestorCashList();

        // Получаем список денег по идентификаторам
        List<InvestorCash> investorCashes = findByIdIn(idsList);

        List<Long> remainingUnderFacilityList = dividedCashDTO.getExcludedUnderFacilitiesIdList();

        // Получаем подобъект, куда надо разделить сумму
        UnderFacility underFacility = underFacilityService.findById(
                dividedCashDTO.getReUnderFacilityId());

        // Получаем объект, в который надо разделить сумму
        Facility facility = facilityService.findById(underFacility.getFacility().getId());

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
                .setScale(2, BigDecimal.ROUND_CEILING);

        // Вычисляем стоимость подобъекта, куда надо разделить сумму
        BigDecimal coastUnderFacility = underFacility.getRooms().stream()
                .map(Room::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_CEILING);

        // Вычисляем % для выделения доли
        BigDecimal divided = coastUnderFacility.divide(coastFacility, 20, BigDecimal.ROUND_CEILING);
        investorCashes = investorCashes
                .stream()
                .filter(f -> null != f.getGivenCash())
                .collect(Collectors.toList());
        int sumsCnt = investorCashes.size();
        sendStatus("Начинаем разделять суммы");
        final int[] counter = {0};
        investorCashes.forEach(f -> {
            counter[0]++;
            sendStatus(String.format("Разделеляем %d из %d сумм", counter[0], sumsCnt));
            BigDecimal invCash = f.getGivenCash();
            BigDecimal sumInUnderFacility = divided.multiply(invCash);
            BigDecimal sumRemainder = invCash.subtract(sumInUnderFacility);
            f.setIsDivide(1);
            InvestorCash cash = new InvestorCash();
            cash.setSource(f.getId().toString());
            cash.setGivenCash(sumInUnderFacility);
            cash.setDateGiven(f.getDateGiven());
            cash.setFacility(f.getFacility());
            cash.setInvestor(f.getInvestor());
            cash.setCashSource(f.getCashSource());
            cash.setNewCashDetail(f.getNewCashDetail());
            cash.setUnderFacility(underFacility);
            cash.setDateClosing(null);
            cash.setTypeClosing(null);
            cash.setShareType(f.getShareType());
            cash.setDateReport(f.getDateReport());
            cash.setSourceFacility(f.getSourceFacility());
            cash.setSourceUnderFacility(f.getSourceUnderFacility());
            cash.setRoom(f.getRoom());
            f.setGivenCash(sumRemainder);
            if (f.getGivenCash().signum() == 0) {
                f.setIsDivide(1);
                f.setIsReinvest(1);
                update(f);
            } else {
                create(f);
            }
            create(cash);
        });
        return new ApiResponse("Разделение сумм прошло успешно");
    }

    /**
     * Подготовить сгруппированный список денег, чтобы не плодить много сумм по объекту/подобъекту
     *
     * @param cashList список денен
     * @param what признак реинвестирование с продажи или нет
     * @return сгрупированный список денег
     */
    public Map<String, InvestorCash> groupInvestorsCash(List<InvestorCash> cashList, String what) {
        Map<String, InvestorCash> map = new HashMap<>(0);
        cashList.forEach(ic -> {
            InvestorCash keyMap;
            if ("sale".equals(what)) {
                keyMap = map.get(ic.getInvestor().getLogin() +
                        ic.getSourceUnderFacility().getName());
            } else {
                keyMap = map.get(ic.getInvestor().getLogin() + ic.getSourceFacility().getName());
            }

            if (keyMap == null) {
                if ("sale".equals(what)) {
                    map.put(ic.getInvestor().getLogin() +
                                    ic.getSourceUnderFacility().getName(),
                            ic);
                } else {
                    map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getName(),
                            ic);
                }

            } else {
                InvestorCash cash = new InvestorCash();
                cash.setGivenCash(ic.getGivenCash().add(keyMap.getGivenCash()));
                cash.setSource(ic.getSource());
                cash.setDateGiven(ic.getDateGiven());
                cash.setFacility(ic.getFacility());
                cash.setUnderFacility(ic.getUnderFacility());
                cash.setInvestor(ic.getInvestor());
                cash.setShareType(ic.getShareType());
                cash.setDateReport(ic.getDateReport());
                cash.setSourceFacility(ic.getSourceFacility());
                cash.setSourceUnderFacility(ic.getSourceUnderFacility());
                if (ic.getSource() != null && keyMap.getSource() != null) {
                    cash.setSource(ic.getSource() + "|" + keyMap.getSource());
                }
                if (ic.getSourceFlowsId() != null && keyMap.getSourceFlowsId() != null) {
                    cash.setSourceFlowsId(ic.getSourceFlowsId() + "|" + keyMap.getSourceFlowsId());
                }
                if ("sale".equals(what)) {
                    map.put(ic.getInvestor().getLogin() +
                            ic.getSourceUnderFacility().getName(), cash);
                } else {
                    map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getName(), cash);
                }

            }
        });
        return map;
    }

    /**
     * Подготовить список денег для реинвестирования
     *
     * @param oldCashList старый список денег
     * @param facilityToReinvestId id объекта, куда реинвестируем
     * @param underFacilityToReinvestId id подобъекта, куда реинвестируем
     * @param shareTypeId id доли
     * @param dateClose дата закрытия вложения
     * @return новый список денег
     */
    public List<InvestorCash> prepareCashToReinvest(List<InvestorCash> oldCashList, Long facilityToReinvestId,
                                                     Long underFacilityToReinvestId, int shareTypeId, Date dateClose) {
        Facility facility = facilityService.findById(facilityToReinvestId);
        UnderFacility underFacility = underFacilityService.findById(underFacilityToReinvestId);
        ShareType shareType = ShareType.fromId(shareTypeId);

        List<InvestorCash> newCashList = new ArrayList<>();
        for (InvestorCash oldCash : oldCashList) {
            InvestorCash newCash = new InvestorCash(oldCash);
            newCash.setFacility(facility);
            newCash.setUnderFacility(underFacility);
            newCash.setShareType(shareType);
            newCash.setSourceId(oldCash.getId());
            newCash.setSourceFacility(oldCash.getFacility());
            newCash.setSourceUnderFacility(oldCash.getUnderFacility());
            newCash.setDateGiven(dateClose);
            newCashList.add(newCash);
        }
        return newCashList;
    }

    /**
     * Реинвестирование денег с продажи (сохранение)
     *
     * @param reinvestCashDTO данные для реинвестирования
     * @return ответ
     */
    public ApiResponse reinvestCash(ReinvestCashDTO reinvestCashDTO) {
        Long facilityToReinvestId = reinvestCashDTO.getFacilityToReinvestId();
        Long underFacilityToReinvestId = reinvestCashDTO.getUnderFacilityToReinvestId();
        int shareTypeId = reinvestCashDTO.getShareTypeId();
        final Date dateClose = reinvestCashDTO.getDateClose();

        List<Long> investorCashIdList = reinvestCashDTO.getInvestorCashIdList();
        List<InvestorCash> oldCashList = findByIdIn(investorCashIdList);
        List<InvestorCash> reinvestedCash = prepareCashToReinvest(oldCashList, facilityToReinvestId, underFacilityToReinvestId, shareTypeId, dateClose);
        final NewCashDetail newCashDetail = newCashDetailService.findByName("Реинвестирование с продажи (сохранение)");
        final TypeClosing typeClosing = typeClosingService.findByName("Реинвестирование");
        final Map<String, InvestorCash> map = groupInvestorsCash(reinvestedCash, "");

        map.forEach((key, value) -> {
            value.setNewCashDetail(newCashDetail);
            value.setGivenCash(value.getGivenCash().setScale(2, RoundingMode.DOWN));
            create(value);
        });

        oldCashList.forEach(f -> {
            f.setIsReinvest(1);
            f.setDateClosing(dateClose);
            f.setTypeClosing(typeClosing);
            create(f);
        });
        return new ApiResponse("Реинвестирование прошло успешно");
    }

    /**
     * Закрыть суммы по инвесторам (массовое)
     *
     * @param closeCashDTO DTO для закрытия сумм
     * @return сообщение
     */
    public ApiResponse close(CloseCashDTO closeCashDTO) {
        AppUser invBuyer = null;
        if (closeCashDTO.getInvestorBuyerId() != null) {
            invBuyer = userService.findById(closeCashDTO.getInvestorBuyerId());
        }

        List<InvestorCash> cashList = new ArrayList<>(0);
        closeCashDTO.getInvestorCashIdList().forEach(id -> cashList.add(findById(id)));

        Date dateClose = closeCashDTO.getDateReinvest();
        Date realDateGiven = closeCashDTO.getRealDateGiven();
        // список сумм, которые закрываем для вывода
        Set<InvestorCash> closeCashes = new HashSet<>();
        // список сумм, которые закрываем для перепродажи доли
        Set<InvestorCash> oldCashes = new HashSet<>();
        // список сумм, которые получатся на выходе
        Set<InvestorCash> newCashes = new HashSet<>();

        for (InvestorCash c : cashList) {
            if (invBuyer != null) { // Перепродажа доли
                TypeClosing closingInvest = typeClosingService.findByName("Перепродажа доли");
                NewCashDetail newCashDetail = newCashDetailService.findByName("Перепокупка доли");
                InvestorCash copyCash = new InvestorCash(c);
                InvestorCash newInvestorCash = new InvestorCash(c);

                copyCash.setInvestor(invBuyer);
                copyCash.setDateGiven(dateClose);
                copyCash.setSourceId(c.getId());
                copyCash.setCashSource(null);
                copyCash.setSource(null);
                copyCash.setNewCashDetail(newCashDetail);
                copyCash.setRealDateGiven(realDateGiven);

                copyCash = createNew(copyCash);

                newInvestorCash.setCashSource(null);
                newInvestorCash.setGivenCash(newInvestorCash.getGivenCash().negate());
                newInvestorCash.setSourceId(c.getId());
                newInvestorCash.setSource(null);
                newInvestorCash.setDateGiven(dateClose);
                newInvestorCash.setDateClosing(dateClose);
                newInvestorCash.setTypeClosing(closingInvest);

                createNew(newInvestorCash);

                c.setDateClosing(dateClose);
                c.setTypeClosing(closingInvest);
                update(c);
                oldCashes.add(c);
                newCashes.add(c);
                newCashes.add(copyCash);
                newCashes.add(newInvestorCash);
            } else {
                TypeClosing cashing = typeClosingService.findByName("Вывод");
                InvestorCash cashForTx = new InvestorCash(c);
                cashForTx.setId(c.getId());
                c.setDateClosing(dateClose);
                c.setTypeClosing(cashing);
                c.setRealDateGiven(realDateGiven);
                update(c);
                closeCashes.add(cashForTx);
            }
        }
        if (closeCashes.size() > 0) {
            transactionLogService.close(closeCashes);
        } else {
            transactionLogService.resale(oldCashes, newCashes);
        }
        return new ApiResponse("Массовое закрытие прошло успешно.");
    }

    private void sendStatus(String message) {
        statusService.sendStatus(message);
    }
}
