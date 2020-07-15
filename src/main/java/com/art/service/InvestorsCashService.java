package com.art.service;

import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.InvestorsTotalSum;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.filters.CashFilter;
import com.art.repository.InvestorsCashRepository;
import com.art.specifications.InvestorsCashSpecification;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class InvestorsCashService {

    private static final String RESALE_SHARE = "Перепродажа доли";
    private final InvestorsCashRepository investorsCashRepository;
    private final InvestorsCashSpecification specification;
    private final TypeClosingInvestService typeClosingInvestService;
    private final AfterCashingService afterCashingService;
    private final UnderFacilityService underFacilityService;

    @Autowired
    public InvestorsCashService(InvestorsCashRepository investorsCashRepository,
                                InvestorsCashSpecification specification,
                                TypeClosingInvestService typeClosingInvestService,
                                AfterCashingService afterCashingService, UnderFacilityService underFacilityService) {
        this.investorsCashRepository = investorsCashRepository;
        this.specification = specification;
        this.typeClosingInvestService = typeClosingInvestService;
        this.afterCashingService = afterCashingService;
        this.underFacilityService = underFacilityService;
    }

    public List<InvestorsCash> findAll() {
        return investorsCashRepository.findAll();
    }

    public InvestorsCash findById(Long id) {
        return investorsCashRepository.findById(id);
    }

    public List<InvestorsCash> findByFacilityId(Long facilityId) {
        return investorsCashRepository.findByFacilityId(facilityId);
    }

    public InvestorsCash update(InvestorsCash investorsCash) {
        investorsCash = investorsCashRepository.saveAndFlush(investorsCash);
        return investorsCash;
    }

    public InvestorsCash createNew(InvestorsCash investorsCash) {
        investorsCash = investorsCashRepository.saveAndFlush(investorsCash);
        return investorsCash;
    }

    public void deleteById(Long id) {
        investorsCashRepository.deleteById(id);
    }

    public void delete(InvestorsCash cash) {
        investorsCashRepository.delete(cash);
    }

    public List<InvestorsTotalSum> getInvestorsCashSums(Long investorId) {
        return investorsCashRepository.getInvestorsCashSums(investorId);
    }

    public void saveAll(List<InvestorsCash> investorsCashes) {
        investorsCashRepository.save(investorsCashes);
    }

    public List<InvestorsCash> findByRoomId(Long roomId) {
        return investorsCashRepository.findByRoomId(roomId);
    }

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public InvestorsCash create(InvestorsCash investorsCash) {
        return this.em.merge(investorsCash);
    }

    public List<InvestorsCash> findByIdIn(List<Long> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(investorsCashRoot.get(InvestorsCash_.id).in(idList));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findBySource(String source) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.source), source));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findBySourceId(Long sourceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.sourceId), sourceId));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorAndFacility(BigInteger investorId, BigInteger facilityId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.and(cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId),
                cb.equal(investorsCashRoot.get(InvestorsCash_.facilityId), facilityId),
                cb.gt(investorsCashRoot.get(InvestorsCash_.givedCash), 0),
                cb.isNull(investorsCashRoot.get(InvestorsCash_.typeClosingInvest))));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorId(Long investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.underFacility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorsCash_.dateGivedCash)));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public Facility getSumCash(BigInteger investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.multiselect(investorsCashRoot.get(InvestorsCash_.facility),
                cb.sum(investorsCashRoot.get(InvestorsCash_.givedCash)));
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.investor).get(AppUser_.id), investorId));
        investorsCashCriteriaQuery.groupBy(investorsCashRoot.get(InvestorsCash_.facility));
        List<InvestorsCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        final Facility[] facility = {new Facility()};
        final BigDecimal[] max = {new BigDecimal(BigInteger.ZERO)};
        cash.forEach(c -> {
            c.setGivedCash(c.getGivedCash().stripTrailingZeros());
            if (c.getGivedCash().compareTo(max[0]) > 0) {
                max[0] = c.getGivedCash();
                facility[0] = c.getFacility();
            }
        });
        return facility[0];
    }

    public List<InvestorsCash> findAllWithAllFields() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.facility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.isNotNull(investorsCashRoot.get(InvestorsCash_.facility)));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorsCash_.dateGivedCash)));
        List<InvestorsCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        cash.forEach(c -> {
            Hibernate.initialize(c.getInvestor());
            Hibernate.initialize(c.getFacility());
            Hibernate.initialize(c.getUnderFacility());
            Hibernate.initialize(c.getRoom());
        });

        return cash;
    }

    public Page<InvestorsCash> findAll(CashFilter filters, Pageable pageable) {
        return investorsCashRepository.findAll(
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
                if (searchSummary.getInvestorsCash() != null) {
                    List<UnderFacility> underFacilities = searchSummary.getUnderFacilityList();
                    InvestorsCash invCash = searchSummary.getInvestorsCash();
                    invCash.setInvestor(user);
                    invCash.setUnderFacility(finalUnderFacility);
                    List<AfterCashing> cashingList = new ArrayList<>(0);
                    final InvestorsCash[] cashForGetting = {searchSummary.getInvestorsCash()};
                    if (cashForGetting[0].getUnderFacility() != null && cashForGetting[0].getUnderFacility().getId() == null) {
                        cashForGetting[0].setUnderFacility(null);
                    }
                    Date dateClosingInvest = cashForGetting[0].getDateGivedCash();
                    List<InvestorsCash> investorsCashes = getMoneyForCashing(cashForGetting[0]);
                    if (investorsCashes.size() == 0) {
                        result[0] = "Нет денег для вывода";
                        return;
                    }
                    final BigDecimal sumCash = investorsCashes.stream().map(InvestorsCash::getGivedCash).reduce(BigDecimal.ZERO, BigDecimal::add); // все деньги инвестора
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
                        cashForGetting[0].setGivedCash(sumCash.subtract(commission));
                    } else {
                        commission = (cashForGetting[0].getGivedCash().multiply(commission)).divide(new BigDecimal(100), BigDecimal.ROUND_CEILING);
                        if (commissionNoMore != null && commission.compareTo(commissionNoMore) > 0) {
                            commission = commissionNoMore;
                        }
                        totalSum = cashForGetting[0].getGivedCash().add(commission);
                        remainderSum[0] = totalSum;
                    }
                    if ((sumCash.compareTo(totalSum)) < 0) {
                        result[0] = "Сумма должна быть не более " + String.valueOf(sumCash).substring(0, String.valueOf(sumCash).indexOf("."));
                        return;
                    }

                    final TypeClosingInvest typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest("Вывод");
                    final TypeClosingInvest typeClosingCommission = typeClosingInvestService.findByTypeClosingInvest("Вывод_комиссия");

                    final InvestorsCash[] commissionCash = {new InvestorsCash()};
                    final InvestorsCash[] cashForManipulate = {null};
                    commissionCash[0].setGivedCash(commission.negate());
                    commissionCash[0].setTypeClosingInvest(typeClosingCommission);
                    commissionCash[0].setInvestor(cashForGetting[0].getInvestor());
                    commissionCash[0].setFacility(cashForGetting[0].getFacility());
                    commissionCash[0].setUnderFacility(cashForGetting[0].getUnderFacility());
                    commissionCash[0].setDateClosingInvest(dateClosingInvest);

                    StringBuilder sourceCash = new StringBuilder();
                    final AtomicInteger[] incr = {new AtomicInteger()};
                    final InvestorsCash[] newCash = {null};

                    if (all) {
                        investorsCashes.forEach(ic -> {
                            cashingList.add(new AfterCashing(ic.getId(), ic.getGivedCash()));
                            ic.setTypeClosingInvest(typeClosingInvest);
                            ic.setDateClosingInvest(dateClosingInvest);
                            if (incr[0].get() == investorsCashes.size() - 1) {
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

                        investorsCashes.forEach(ic -> {
                            if (remainderSum[0].equals(BigDecimal.ZERO)) return;
                            cashingList.add(new AfterCashing(ic.getId(), ic.getGivedCash()));
                            if (incr[0].get() == investorsCashes.size() - 1) {
                                sourceCash.append(ic.getId().toString());
                            } else {
                                sourceCash.append(ic.getId().toString()).append("|");
                            }
                            // если сумма остатка, который надо вывести, больше текущей суммы инвестора
                            if (ic.getGivedCash().subtract(remainderSum[0]).compareTo(BigDecimal.ZERO) < 0) {
                                // остаток = остаток - текущая сумма инвестора
                                remainderSum[0] = remainderSum[0].subtract(ic.getGivedCash());
                                ic.setDateClosingInvest(dateClosingInvest);
                                ic.setTypeClosingInvest(typeClosingInvest);
                                update(ic);
                            } else {
                                // иначе если сумма остатка, который надо вывести, меньше текущей суммы инвестора
                                // создаём проводку, с которой сможем в дальнейшем проводить какие-либо действия
                                // на сумму (текущие деньги вычитаем сумму остатка и комиссию)
                                cashForManipulate[0] = new InvestorsCash(ic);
                                cashForManipulate[0].setGivedCash(ic.getGivedCash().subtract(remainderSum[0]));
                                // основную сумму блокируем для операций
                                ic.setGivedCash(BigDecimal.ZERO);
                                ic.setIsReinvest(1);
                                ic.setIsDivide(1);
                                // сохраняем сумму
                                update(ic);

                                // создаём новую сумму на остаток + комиссия
                                newCash[0] = new InvestorsCash(ic);
                                newCash[0].setGivedCash(remainderSum[0]);
                                newCash[0].setDateClosingInvest(dateClosingInvest);
                                newCash[0].setTypeClosingInvest(typeClosingInvest);
                                remainderSum[0] = BigDecimal.ZERO;
                                fillCash(commissionCash[0], ic);
                                fillCash(cashForGetting[0], ic);
                            }
                            incr[0].getAndIncrement();
                        });
                    }

                    cashForGetting[0].setGivedCash(cashForGetting[0].getGivedCash().negate());
                    cashForGetting[0].setDateClosingInvest(commissionCash[0].getDateClosingInvest());
                    cashForGetting[0].setTypeClosingInvest(typeClosingInvest);

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

    private void fillCash(InvestorsCash to, InvestorsCash from) {
        to.setDateGivedCash(from.getDateGivedCash());
        to.setCashSource(from.getCashSource());
//        to.setCashType(from.getCashType());
        to.setNewCashDetails(from.getNewCashDetails());
//        to.setInvestorsType(from.getInvestorsType());
        to.setShareType(from.getShareType());
        to.setDateReport(from.getDateReport());
        to.setSourceFacility(from.getSourceFacility());
        to.setSourceUnderFacility(from.getSourceUnderFacility());
        to.setSourceFlowsId(from.getSourceFlowsId());
        to.setRoom(from.getRoom());
    }

    public List<InvestorsCash> getMoneyForCashing(InvestorsCash cashForGetting) {
        CashFilter filter = new CashFilter();
        filter.setInvestor(cashForGetting.getInvestor());
        filter.setFacility(cashForGetting.getFacility().getName());
        if (!Objects.equals(null, cashForGetting.getUnderFacility())) {
            filter.setUnderFacility(cashForGetting.getUnderFacility().getName());
        }
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
        return investorsCashRepository.findAll(
                specification.getFilterForCashing(filter), pageable).getContent();

    }

    public List<InvestorsCash> getInvestedMoney() {
        TypeClosingInvest typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest(RESALE_SHARE);
        return investorsCashRepository.findAll(specification.getInvestedMoney(typeClosingInvest.getId()));
    }
}
