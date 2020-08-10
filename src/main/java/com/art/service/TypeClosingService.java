package com.art.service;

import com.art.config.application.Constant;
import com.art.model.TypeClosing;
import com.art.repository.TypeClosingRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TypeClosingService {

    private final TypeClosingRepository typeClosingRepository;

    public TypeClosingService(TypeClosingRepository typeClosingRepository) {
        this.typeClosingRepository = typeClosingRepository;
    }

    @Cacheable(Constant.TYPES_CLOSING_CACHE_KEY)
    public List<TypeClosing> findAll() {
        return typeClosingRepository.findAll();
    }

    @Cacheable(Constant.TYPES_CLOSING_CACHE_KEY)
    public TypeClosing findById(Long id) {
        return typeClosingRepository.findOne(id);
    }

    @Cacheable(Constant.TYPES_CLOSING_CACHE_KEY)
    public TypeClosing findByName(String name) {
        return typeClosingRepository.findByName(name);
    }

    @CacheEvict(Constant.TYPES_CLOSING_CACHE_KEY)
    public void deleteById(Long id) {
        typeClosingRepository.delete(id);
    }

    @CachePut(value = Constant.TYPES_CLOSING_CACHE_KEY, key = "#typeClosing.id")
    public void update(TypeClosing typeClosing) {
        typeClosingRepository.saveAndFlush(typeClosing);
    }

    @CachePut(Constant.TYPES_CLOSING_CACHE_KEY)
    public void create(TypeClosing typeClosing) {
        typeClosingRepository.saveAndFlush(typeClosing);
    }

    public List<TypeClosing> init() {
        TypeClosing typeClosing = new TypeClosing();
        typeClosing.setId(0L);
        typeClosing.setName("Выберите вид закрытия");
        List<TypeClosing> typeClosingList = new ArrayList<>(0);
        typeClosingList.add(typeClosing);
        typeClosingList.addAll(findAll());
        return typeClosingList;
    }
}
