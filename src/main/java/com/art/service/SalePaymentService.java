package com.art.service;

import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.AccountTxDTO;
import com.art.model.supporting.dto.SalePaymentDTO;
import com.art.model.supporting.dto.SalePaymentDivideDTO;
import com.art.model.supporting.filters.SalePaymentFilter;
import com.art.repository.*;
import com.art.specifications.SalePaymentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class SalePaymentService {

    private final SalePaymentRepository salePaymentRepository;

    private final SalePaymentSpecification saleSpecification;

    private final FacilityRepository facilityRepository;

    private final UnderFacilityRepository underFacilityRepository;

    private final MoneyRepository moneyRepository;

    private final TransactionLogService txLogService;

    private final NewCashDetailRepository newCashDetailRepository;

    private final AccountTransactionService accountTransactionService;

    @Autowired
    public SalePaymentService(SalePaymentRepository salePaymentRepository,
                              SalePaymentSpecification saleSpecification, FacilityRepository facilityRepository,
                              UnderFacilityRepository underFacilityRepository, MoneyRepository moneyRepository,
                              TransactionLogService txLogService, NewCashDetailRepository newCashDetailRepository,
                              AccountTransactionService accountTransactionService) {
        this.salePaymentRepository = salePaymentRepository;
        this.saleSpecification = saleSpecification;
        this.facilityRepository = facilityRepository;
        this.underFacilityRepository = underFacilityRepository;
        this.moneyRepository = moneyRepository;
        this.txLogService = txLogService;
        this.newCashDetailRepository = newCashDetailRepository;
        this.accountTransactionService = accountTransactionService;
    }

//    @CachePut(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public void create(SalePayment sale) {
        salePaymentRepository.save(sale);
    }

//    @CachePut(value = Constant.INVESTOR_FLOWS_SALE_CACHE_KEY, key = "#sale.id")
    public void update(SalePayment sale) {
        salePaymentRepository.save(sale);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    public List<SalePayment> findAll() {
        return salePaymentRepository.findAll();
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    private List<SalePayment> findByIdIn(List<Long> idList) {
        return salePaymentRepository.findByIdIn(idList);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    private SalePayment findById(Long id) {
        return salePaymentRepository.findOne(id);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    private void deleteById(Long id) {
        salePaymentRepository.delete(id);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_SALE_CACHE_KEY)
    private List<SalePayment> findBySourceId(Long sourceId) {
        return salePaymentRepository.findBySourceId(sourceId);
    }

    private SalePayment findParentFlow(SalePayment flowsSale, List<SalePayment> childFlows) {
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
    public Page<SalePayment> findAll(SalePaymentFilter filters, Pageable pageable) {
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
        AccountTxDTO accountTxDTO = new AccountTxDTO();
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
                AccountTransaction transaction = ltd.getTransaction();
                if (transaction != null) {
                    accountTxDTO.addTxId(transaction.getId());
                }
            });
            if (accountTxDTO.getTxIds() != null && !accountTxDTO.getTxIds().isEmpty()) {
                accountTxDTO.getTxIds().forEach(accountTransactionService::deleteById);
            }
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

    /**
     * Сохранить список выплат с продажи
     *
     * @param salePayments список
     */
    public void saveAll(List<SalePayment> salePayments) {
        salePaymentRepository.save(salePayments);
    }

}
