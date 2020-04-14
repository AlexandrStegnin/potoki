package com.art.service;

import com.art.model.AppToken;
import com.art.repository.AppTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class AppTokenService {

    private final AppTokenRepository appTokenRepository;

    public AppTokenService(AppTokenRepository appTokenRepository) {
        this.appTokenRepository = appTokenRepository;
    }

    public List<AppToken> findAll() {
        return appTokenRepository.findAll();
    }

    public AppToken findById(Long id) {
        return appTokenRepository.findOne(id);
    }

    public AppToken findByAppName(String appName) {
        return appTokenRepository.findByAppName(appName);
    }

    public void delete(AppToken token) {
        appTokenRepository.delete(token);
    }

    public void delete(Long id) {
        appTokenRepository.delete(id);
    }

    public AppToken update(AppToken token) {
        return appTokenRepository.save(token);
    }

    public AppToken create(AppToken token) {
        return appTokenRepository.save(token);
    }

}
