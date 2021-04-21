package com.art.repository;

import com.art.model.supporting.AppFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AppFilterRepository extends JpaRepository<AppFilter, Long> {

    AppFilter findByUserIdAndPageId(Long userId, Integer pageId);

}
