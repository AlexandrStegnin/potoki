package com.art.service;

import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.SalePaymentDTO;
import com.art.model.supporting.dto.SalePaymentDivideDTO;
import com.art.model.supporting.filters.FlowsSaleFilter;
import com.art.repository.*;
import com.art.specifications.SalePaymentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class SalePaymentService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final SalePaymentRepository salePaymentRepository;

    private final SalePaymentSpecification saleSpecification;

    private final FacilityRepository facilityRepository;

    private final UnderFacilityRepository underFacilityRepository;

    private final MoneyRepository moneyRepository;

    private final TransactionLogService txLogService;

    private final NewCashDetailRepository newCashDetailRepository;

    @Autowired
    public SalePaymentService(SalePaymentRepository salePaymentRepository,
                              SalePaymentSpecification saleSpecification, FacilityRepository facilityRepository,
                              UnderFacilityRepository underFacilityRepository, MoneyRepository moneyRepository,
                              TransactionLogService txLogService, NewCashDetailRepository newCashDetailRepository) {
        this.salePaymentRepository = salePaymentRepository;
        this.saleSpecification = saleSpecification;
        this.facilityRepository = facilityRepository;
        this.underFacilityRepository = underFacilityRepository;
        this.moneyRepository = moneyRepository;
        this.txLogService = txLogService;
        this.newCashDetailRepository = newCashDetailRepository;
    }

//    @CachePut(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void create(SalePayment sale) {
        salePaymentRepository.save(sale);
    }

//    @CachePut(value = Constant.INVESTOR_FLOWS_SALE_CACHE_KEY, key = "#sale.id")
    public void update(SalePayment sale) {
        this.em.merge(sale);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> saleCriteriaQuery = cb.createQuery(SalePayment.class);
        Root<SalePayment> saleRoot = saleCriteriaQuery.from(SalePayment.class);
        saleRoot.fetch(SalePayment_.facility, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.investor, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findByIdInWithAllFields(List<Long> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> saleCriteriaQuery = cb.createQuery(SalePayment.class);
        Root<SalePayment> saleRoot = saleCriteriaQuery.from(SalePayment.class);
        saleRoot.fetch(SalePayment_.facility, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.investor, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(saleRoot.get(SalePayment_.id).in(idList));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findByIdIn(List<Long> idList) {
        return salePaymentRepository.findByIdIn(idList);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public SalePayment findById(Long id) {
        return this.em.find(SalePayment.class, id);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void deleteById(Long id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<SalePayment> delete = cb.createCriteriaDelete(SalePayment.class);
        Root<SalePayment> flowsSaleRoot = delete.from(SalePayment.class);
        delete.where(cb.equal(flowsSaleRoot.get(SalePayment_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findBySourceId(Long sourceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> saleCriteriaQuery = cb.createQuery(SalePayment.class);
        Root<SalePayment> saleRoot = saleCriteriaQuery.from(SalePayment.class);
        saleRoot.fetch(SalePayment_.facility, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.investor, JoinType.LEFT);
        saleRoot.fetch(SalePayment_.underFacility, JoinType.LEFT);
        saleCriteriaQuery.select(saleRoot).distinct(true);
        saleCriteriaQuery.where(cb.equal(saleRoot.get(SalePayment_.sourceId), sourceId));
        return em.createQuery(saleCriteriaQuery).getResultList();
    }

    public SalePayment findParentFlow(SalePayment flowsSale, List<SalePayment> childFlows) {
        if (!Objects.equals(null, flowsSale.getSourceId())) {
            childFlows.add(flowsSale);
            SalePayment finalFlowsSale = flowsSale;
            flowsSale = findById(finalFlowsSale.getSourceId());
            return findParentFlow(flowsSale, childFlows);
        }
        return flowsSale;
    }

    public List<SalePayment> findAllChildes(SalePayment parentFlow, List<SalePayment> childFlows, int next) {
        List<SalePayment> tmp = findBySourceId(parentFlow.getId());
        tmp.forEach(t -> {
            if (!childFlows.contains(t)) {
                childFlows.add(t);
            }
        });
        if (next >= childFlows.size()) next--;
        if (next < 0) return childFlows;
        SalePayment parent = childFlows.get(next);
        if (parentFlow.getId().equals(parent.getId())) return childFlows;
        next++;
        return findAllChildes(parent, childFlows, next);

    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public Page<SalePayment> findAll(FlowsSaleFilter filters, Pageable pageable) {
        if (filters.getPageSize() == 0) pageable = new PageRequest(filters.getPageNumber(), filters.getTotal() + 1);
        return salePaymentRepository.findAll(
                saleSpecification.getFilter(filters),
                pageable
        );
    }

    /**
     * Удалить список выплат (продажа)
     *
     * @param dto DTO со списком сумм для удаления
     * @return ответ
     */
    public ApiResponse deleteChecked(SalePaymentDTO dto) {
        ApiResponse response;
        try {
            List<Long> deletedChildesIds = new ArrayList<>();
            List<SalePayment> listToDelete = findByIdIn(dto.getSalePaymentsId());
            listToDelete.forEach(ltd -> {
                if (!deletedChildesIds.contains(ltd.getId())) {
                    List<SalePayment> childFlows = new ArrayList<>();
                    SalePayment parentFlow = findParentFlow(ltd, childFlows);
                    if (parentFlow.getIsReinvest() == 1) parentFlow.setIsReinvest(0);
                    childFlows = findAllChildes(parentFlow, childFlows, 0);
                    childFlows.sort(Comparator.comparing(SalePayment::getId).reversed());
                    childFlows.forEach(cf -> {
                        deletedChildesIds.add(cf.getId());
                        parentFlow.setProfitToReInvest(parentFlow.getProfitToReInvest().add(cf.getProfitToReInvest()));
                        deleteById(cf.getId());
                        update(parentFlow);
                    });
                    if (parentFlow.getId().equals(ltd.getId())) {
                        deleteById(parentFlow.getId());
                    }
                }
                List<Money> monies = moneyRepository.findBySourceFlowsId(String.valueOf(ltd.getId()));
                moneyRepository.delete(monies);
            });
            response = new ApiResponse("Данные по выплатам с продажи успешно удалены");
        } catch (Exception ex) {
            response = new ApiResponse(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    /**
     * Реинвестировать суммы по выплатам (продажа)
     *
     * @param dto DTO сумм к реинвестирования
     * @return ответ об успешном окончании
     */
    public ApiResponse reinvest(SalePaymentDTO dto) {
        List<SalePayment> salePayments = salePaymentRepository.findByIdIn(dto.getSalePaymentsId());
        Set<Money> monies = new HashSet<>();
        Facility facility = facilityRepository.findOne(dto.getFacilityId());
        UnderFacility underFacility = underFacilityRepository.findOne(dto.getUnderFacilityId());
        NewCashDetail newCashDetail = newCashDetailRepository.findByName("Реинвестирование с продажи (прибыль)");
        salePayments.forEach(salePayment -> {
            Money money = new Money(salePayment, dto, facility, underFacility, newCashDetail);
            money = moneyRepository.saveAndFlush(money);
            monies.add(money);
            salePayment.setIsReinvest(1);
            salePaymentRepository.saveAndFlush(salePayment);
        });
        txLogService.reinvestmentSale(salePayments, monies);
        return new ApiResponse("Реинвестирование денег с продажи прошло успешно");
    }

    /**
     * Выделить сумму из выплаты (продажа)
     *
     * @param divideDTO DTO для выделения суммы
     * @return ответ
     */
    public ApiResponse divideSalePayment(SalePaymentDivideDTO divideDTO) {
        Long salePaymentId = divideDTO.getSalePaymentId();
        BigDecimal divideSum = divideDTO.getExtractedSum();
        SalePayment oldFlows = findById(salePaymentId);
        SalePayment newFlows = new SalePayment(oldFlows);
        oldFlows.setProfitToReInvest(oldFlows.getProfitToReInvest().subtract(divideSum));
        newFlows.setProfitToReInvest(divideSum);
        newFlows.setSourceId(oldFlows.getId());
        if (oldFlows.getProfitToReInvest().compareTo(BigDecimal.ZERO) <= 0) oldFlows.setIsReinvest(1);
        update(oldFlows);
        create(newFlows);
        return new ApiResponse(oldFlows.getProfitToReInvest().toPlainString());
    }
}
