package com.art.service;

import com.art.model.TypeClosing;
import com.art.repository.TypeClosingRepository;
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

    public List<TypeClosing> findAllWithCriteriaApi() {
        return typeClosingRepository.findAll();
    }

    public TypeClosing findById(Long id) {
        return typeClosingRepository.findOne(id);
    }

    public TypeClosing findByName(String name) {
        return typeClosingRepository.findByName(name);
    }

    public void deleteById(Long id) {
        typeClosingRepository.delete(id);
    }

    public void update(TypeClosing typeClosing) {
        typeClosingRepository.saveAndFlush(typeClosing);
    }

    public void create(TypeClosing typeClosing) {
        typeClosingRepository.saveAndFlush(typeClosing);
    }

    public List<TypeClosing> init() {
        TypeClosing typeClosing = new TypeClosing();
        typeClosing.setId(0L);
        typeClosing.setName("Выберите вид закрытия");
        List<TypeClosing> typeClosingList = new ArrayList<>(0);
        typeClosingList.add(typeClosing);
        typeClosingList.addAll(typeClosingRepository.findAll());
        return typeClosingList;
    }
}
