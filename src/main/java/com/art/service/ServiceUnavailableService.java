package com.art.service;

import com.art.model.supporting.ServiceUnavailable;
import com.art.repository.ServiceUnavailableRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
@Transactional
@Repository
public class ServiceUnavailableService {
    @Resource(name = "serviceUnavailableRepository")
    private ServiceUnavailableRepository serviceUnavailableRepository;

    public ServiceUnavailable update(ServiceUnavailable serviceUnavailable) {
        return serviceUnavailableRepository.save(serviceUnavailable);
    }

    public ServiceUnavailable findServiceUnavailable() {
        return serviceUnavailableRepository.findFirstByIdIsNotNull();
    }

    public int getServiceUnavailable(BigInteger id) {
        return serviceUnavailableRepository.findById(id).getStatus();
    }
}
