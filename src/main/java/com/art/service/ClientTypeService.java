package com.art.service;

import com.art.config.exception.EntityNotFoundException;
import com.art.model.ClientType;
import com.art.repository.ClientTypeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientTypeService {

    ClientTypeRepository clientTypeRepository;

    public ClientTypeService(ClientTypeRepository clientTypeRepository) {
        this.clientTypeRepository = clientTypeRepository;
    }

    public List<ClientType> findAll() {
        return clientTypeRepository.findAll();
    }

    public ClientType findById(Long id) {
        ClientType clientType = clientTypeRepository.findOne(id);
        if (null == clientType) {
            throw new EntityNotFoundException("Вид клиента с id = [" + id + "] не найден");
        }
        return clientType;
    }

    public ClientType update(ClientType clientType) {
        return clientTypeRepository.save(clientType);
    }

    public ClientType create(ClientType clientType) {
        return clientTypeRepository.save(clientType);
    }

    public void delete(ClientType clientType) {
        clientTypeRepository.delete(clientType);
    }

    public void delete(Long id) {
        clientTypeRepository.delete(id);
    }

}
