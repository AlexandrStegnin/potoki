package com.art.service;

import com.art.config.exception.EntityNotFoundException;
import com.art.model.TypeClosing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.TypeClosingDTO;
import com.art.repository.TypeClosingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.art.config.application.Constant.NEW_CASH_DETAIL_REINVEST;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TypeClosingService {

  TypeClosingRepository typeClosingRepository;

  public List<TypeClosing> findAll() {
    return typeClosingRepository.findAll();
  }

  public TypeClosing findById(Long id) {
    return typeClosingRepository.findOne(id);
  }

  public TypeClosing findByName(String name) {
    TypeClosing typeClosing = typeClosingRepository.findByName(name);
    if (Objects.isNull(typeClosing)) {
      throw new EntityNotFoundException(String.format("Вид закрытия %s не найден", name));
    }
    return typeClosing;
  }

  public ApiResponse deleteById(Long id) {
    typeClosingRepository.delete(id);
    return new ApiResponse("Детали новых денег успешно удалены.");
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
