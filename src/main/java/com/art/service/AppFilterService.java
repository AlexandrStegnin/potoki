package com.art.service;

import com.art.config.SecurityUtils;
import com.art.model.supporting.AppFilter;
import com.art.model.supporting.enums.AppPage;
import com.art.model.supporting.filters.AbstractFilter;
import com.art.repository.AppFilterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppFilterService {

    ObjectMapper objectMapper;

    AppFilterRepository appFilterRepository;

    public AppFilter findFilter(Long userId, Integer pageId) {
        return appFilterRepository.findByUserIdAndPageId(userId, pageId);
    }

    public void save(AppFilter appFilter) {
        appFilterRepository.save(appFilter);
    }

    /**
     * Получить подготовленный фильтр из базы данных
     *
     * @param oldFilter старый фильтр
     * @param clazz класс, в который надо прочитать фильтр
     * @param page страница для которой запрашивается фильтр
     * @return полученный или старый фильтр
     */
    public AbstractFilter getFilter(AbstractFilter oldFilter, Class<? extends AbstractFilter> clazz, AppPage page) {
        Long curUserId = SecurityUtils.getUserId();
        AppFilter appFilter = findFilter(curUserId, page.getId());
        if (Objects.nonNull(appFilter)) {
            try {
                return objectMapper.readValue(appFilter.getText(), clazz);
            } catch (JsonProcessingException e) {
                log.trace("Не удалось получить фильтр: " + e.getMessage());
            }
        }
        return oldFilter;
    }

    /**
     * Обновить инфо о фильтрах в базе данных
     *
     * @param filter фильтры
     */
    public void updateFilter(AbstractFilter filter, AppPage page) {
        Long curUserId = SecurityUtils.getUserId();
        AppFilter appFilter = findFilter(curUserId, page.getId());
        if (Objects.isNull(appFilter)) {
            appFilter = new AppFilter();
            appFilter.setUserId(curUserId);
            appFilter.setPageId(page);
        }
        try {
            appFilter.setText(objectMapper.writeValueAsString(filter));
        } catch (JsonProcessingException e) {
            log.error("Не удалось распарсить фильтр в строку");
        }
        save(appFilter);
    }

}
