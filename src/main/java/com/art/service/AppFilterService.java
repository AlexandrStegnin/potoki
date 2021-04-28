package com.art.service;

import com.art.model.supporting.AppFilter;
import com.art.repository.AppFilterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * @author Alexandr Stegnin
 */

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppFilterService {

    AppFilterRepository appFilterRepository;

    public AppFilter findFilter(Long userId, Integer pageId) {
        return appFilterRepository.findByUserIdAndPageId(userId, pageId);
    }

    public void save(AppFilter appFilter) {
        appFilterRepository.save(appFilter);
    }
}
