package com.art.service;

import com.art.model.TypeClosing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.TypeClosingDTO;
import com.art.repository.TypeClosingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.art.config.application.Constant.NEW_CASH_DETAIL_REINVEST;

@Service
@Transactional
public class TypeClosingService {

    private final TypeClosingRepository typeClosingRepository;

    public TypeClosingService(TypeClosingRepository typeClosingRepository) {
        this.typeClosingRepository = typeClosingRepository;
    }

//    @Cacheable(Constant.TYPES_CLOSING_CACHE_KEY)
    public List<TypeClosing> findAll() {
        return typeClosingRepository.findAll();
    }

//    @Cacheable(Constant.TYPES_CLOSING_CACHE_KEY)
    public TypeClosing findById(Long id) {
        return typeClosingRepository.findOne(id);
    }

//    @Cacheable(Constant.TYPES_CLOSING_CACHE_KEY)
    public TypeClosing findByName(String name) {
        return typeClosingRepository.findByName(name);
    }

//    @CacheEvict(Constant.TYPES_CLOSING_CACHE_KEY)
    public ApiResponse deleteById(Long id) {
        typeClosingRepository.delete(id);
        return new ApiResponse("Детали новых денег успешно удалены.");
    }

//    @CachePut(value = Constant.TYPES_CLOSING_CACHE_KEY, key = "#typeClosing.id")
    public void update(TypeClosing typeClosing) {
        typeClosingRepository.saveAndFlush(typeClosing);
    }

//    @CachePut(Constant.TYPES_CLOSING_CACHE_KEY)
    public void create(TypeClosing typeClosing) {
        typeClosingRepository.saveAndFlush(typeClosing);
    }

    public List<TypeClosing> init() {
        TypeClosing typeClosing = new TypeClosing();
        typeClosing.setId(0L);
        typeClosing.setName("Выберите вид закрытия");
        List<TypeClosing> typeClosingList = new ArrayList<>(0);
        typeClosingList.add(typeClosing);
        List<TypeClosing> typeClosings = findAll();
        typeClosingList.addAll(typeClosings.stream()
                .filter(tc -> !tc.getName().equalsIgnoreCase("Вывод_комиссия") &&
                        !tc.getName().equalsIgnoreCase(NEW_CASH_DETAIL_REINVEST))
                .collect(Collectors.toList()));
        return typeClosingList;
    }

    public ApiResponse create(TypeClosingDTO dto) {
        TypeClosing typeClosing = new TypeClosing(dto);
        typeClosingRepository.save(typeClosing);
        return new ApiResponse("Вид закрытия успешно создан");
    }

    public ApiResponse update(TypeClosingDTO dto) {
        TypeClosing typeClosing = new TypeClosing(dto);
        typeClosingRepository.save(typeClosing);
        return new ApiResponse("Вид закрытия успешно обновлён");
    }
}
