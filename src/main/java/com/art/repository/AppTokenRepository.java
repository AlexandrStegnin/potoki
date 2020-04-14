package com.art.repository;

import com.art.model.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AppTokenRepository extends JpaRepository<AppToken, Long> {

    AppToken findByAppName(String appName);

}
