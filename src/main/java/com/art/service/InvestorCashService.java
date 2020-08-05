package com.art.service;

import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.SearchSummary;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class InvestorCashService {

    private static final String RESALE_SHARE = "Перепродажа доли";
    private final InvestorCashRepository investorCashRepository;
    private final InvestorCashSpecification specification;
    private final TypeClosingService typeClosingService;
    private final AfterCashingService afterCashingService;
    private final UnderFacilityService underFacilityService;

    @Autowired
    public InvestorCashService(InvestorCashRepository investorCashRepository,
                               InvestorCashSpecification specification,
                               TypeClosingService typeClosingService,
                               AfterCashingService afterCashingService, UnderFacilityService underFacilityService) {
        this.investorCashRepository = investorCashRepository;
        this.specification = specification;
        this.typeClosingService = typeClosingService;
        this.afterCashingService = afterCashingService;
        this.underFacilityService = underFacilityService;
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
}
