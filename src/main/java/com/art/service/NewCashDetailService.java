package com.art.service;

import com.art.config.exception.EntityNotFoundException;
import com.art.model.NewCashDetail;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.NewCashDetailDTO;
import com.art.repository.NewCashDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewCashDetailService {

  NewCashDetailRepository newCashDetailRepository;

  public NewCashDetail create(NewCashDetail newCashDetail) {
    return newCashDetailRepository.saveAndFlush(newCashDetail);
  }

  public List<NewCashDetail> findAll() {
    return newCashDetailRepository.findAll();
  }

  public NewCashDetail findById(Long id) {
    return newCashDetailRepository.findOne(id);
  }

  public NewCashDetail update(NewCashDetail newCashDetail) {
    return newCashDetailRepository.saveAndFlush(newCashDetail);
  }

  public NewCashDetail findByName(String name) {
    NewCashDetail newCashDetail = newCashDetailRepository.findByName(name);
    if (Objects.isNull(newCashDetail)) {
      throw new EntityNotFoundException(String.format("Не найдены детали новых денег %s", name));
    }
    return newCashDetail;
  }

  public void deleteById(Long id) {
    newCashDetailRepository.delete(id);
  }

  public List<NewCashDetail> initializeNewCashDetails() {
    List<NewCashDetail> newCashDetailList = new ArrayList<>(0);
    NewCashDetail newCashDetail = new NewCashDetail();
    newCashDetail.setId(0L);
    newCashDetail.setName("Выберите детали новых денег");
    newCashDetailList.add(newCashDetail);
    newCashDetailList.addAll(findAll());
    return newCashDetailList;
  }

  public ApiResponse create(NewCashDetailDTO dto) {
    NewCashDetail entity = new NewCashDetail(dto);
    newCashDetailRepository.save(entity);
    return new ApiResponse("Детали новых денег успешно созданы");
  }

  public ApiResponse update(NewCashDetailDTO dto) {
    NewCashDetail detail = new NewCashDetail(dto);
    newCashDetailRepository.save(detail);
    return new ApiResponse("Детали новых денег успешно обновлены");
  }
}
