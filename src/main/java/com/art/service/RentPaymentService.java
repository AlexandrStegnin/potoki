package com.art.service;

import com.art.model.RentPayment;
import com.art.model.supporting.filters.RentPaymentFilter;
import com.art.repository.RentPaymentRepository;
import com.art.specifications.RentPaymentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class RentPaymentService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final RentPaymentRepository rentPaymentRepository;

    private final RentPaymentSpecification specification;

    @Autowired
    public RentPaymentService(RentPaymentRepository rentPaymentRepository, RentPaymentSpecification specification) {
        this.rentPaymentRepository = rentPaymentRepository;
        this.specification = specification;
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public List<RentPayment> findAll() {
        return rentPaymentRepository.findAll();
    }

    public List<RentPayment> findByRoomId(Long roomId) {
        return rentPaymentRepository.findByRoomId(roomId);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public void delete() {
        rentPaymentRepository.deleteAll();
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
    public RentPayment findById(BigInteger id) {
        return this.em.find(RentPayment.class, id);
    }

//    @CachePut(value = Constant.INVESTOR_FLOWS_CACHE_KEY, key = "#flows.id")
    public void update(RentPayment flows) {
        rentPaymentRepository.save(flows);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public void deleteByIdIn(List<Long> idList) {
        idList.forEach(rentPaymentRepository::delete);
    }

    public void updateInvestorDemo() {
        Query q = em.createNativeQuery("{call UPDATE_INVESTOR_DEMO()}");
        q.executeUpdate();
    }

    public Page<RentPayment> findAll(RentPaymentFilter filters, Pageable pageable) {
        if (filters.getPageSize() == 0) pageable = new PageRequest(filters.getPageNumber(), filters.getTotal() + 1);
        return rentPaymentRepository.findAll(
                specification.getFilter(filters),
                pageable
        );
    }
}
