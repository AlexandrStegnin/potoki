package com.art.service;

import com.art.model.AccountTransaction;
import com.art.model.Facility;
import com.art.model.Money;
import com.art.model.RentPayment;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.AccountTxDTO;
import com.art.model.supporting.dto.RentPaymentDTO;
import com.art.model.supporting.filters.RentPaymentFilter;
import com.art.repository.FacilityRepository;
import com.art.repository.MoneyRepository;
import com.art.repository.RentPaymentRepository;
import com.art.specifications.RentPaymentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RentPaymentService {

    private final RentPaymentRepository rentPaymentRepository;

    private final RentPaymentSpecification specification;

    private final FacilityRepository facilityRepository;

    private final MoneyRepository moneyRepository;

    private final TransactionLogService txLogService;

    private final AccountTransactionService accountTransactionService;

    @Autowired
    public RentPaymentService(RentPaymentRepository rentPaymentRepository, RentPaymentSpecification specification,
                              FacilityRepository facilityRepository, MoneyRepository moneyRepository,
                              TransactionLogService txLogService, AccountTransactionService accountTransactionService) {
        this.rentPaymentRepository = rentPaymentRepository;
        this.specification = specification;
        this.facilityRepository = facilityRepository;
        this.moneyRepository = moneyRepository;
        this.txLogService = txLogService;
        this.accountTransactionService = accountTransactionService;
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public List<RentPayment> findAll() {
        return rentPaymentRepository.findAll();
    }

    public List<RentPayment> findByRoomId(Long roomId) {
        return rentPaymentRepository.findByRoomId(roomId);
    }

//    @CachePut(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public void saveList(List<RentPayment> rentPaymentList) {
        rentPaymentRepository.save(rentPaymentList);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public List<RentPayment> findByIdIn(List<Long> idList) {
        return rentPaymentRepository.findByIdIn(idList);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public RentPayment findById(Long id) {
        return rentPaymentRepository.findOne(id);
    }

//    @CachePut(value = Constant.INVESTOR_FLOWS_CACHE_KEY, key = "#flows.id")
    public void update(RentPayment flows) {
        rentPaymentRepository.save(flows);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public void deleteByIdIn(List<Long> idList) {
        AccountTxDTO accountTxDTO = new AccountTxDTO();
        idList.forEach(id -> {
            List<Money> monies = moneyRepository.findBySourceFlowsId(String.valueOf(id));
            RentPayment rentPayment = findById(id);
            moneyRepository.delete(monies);
            Long accTxId = rentPayment.getAccTxId();
            if (accTxId != null) {
                AccountTransaction transaction = accountTransactionService.findById(accTxId);
                accountTxDTO.addTxId(accTxId);
                transaction.removeRentPayment(rentPayment);
            }
            rentPaymentRepository.delete(id);
        });
        if (accountTxDTO.getTxIds() != null && !accountTxDTO.getTxIds().isEmpty()) {
            accountTransactionService.delete(accountTxDTO);
        }
    }

    public Page<RentPayment> findAll(RentPaymentFilter filters, Pageable pageable) {
        if (filters.getPageSize() == 0) pageable = new PageRequest(filters.getPageNumber(), filters.getTotal() + 1);
        return rentPaymentRepository.findAll(
                specification.getFilter(filters),
                pageable
        );
    }

    /**
     * Реинвестировать суммы с аренды
     *
     * @param dto DTO для реинвестирования
     * @return сообщение об успешном/неудачном завершении операции
     */
    public ApiResponse reinvest(RentPaymentDTO dto) {
        List<RentPayment> rentPayments = rentPaymentRepository.findByIdIn(dto.getRentPaymentsId());
        Set<Money> monies = new HashSet<>();
        Facility facility = facilityRepository.findOne(dto.getFacilityId());
        rentPayments.forEach(rentPayment -> {
            Money money = new Money(rentPayment, dto, facility);
            money = moneyRepository.saveAndFlush(money);
            monies.add(money);
            rentPayment.setIsReinvest(1);
            rentPaymentRepository.saveAndFlush(rentPayment);
        });
        txLogService.reinvestmentRent(rentPayments, monies);
        return new ApiResponse("Реинвестирование денег с аренды прошло успешно");
    }

    public void create(RentPayment rentPayment) {
        rentPaymentRepository.save(rentPayment);
    }

}
