package com.art.service;

import com.art.model.AppToken;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.TokenDTO;
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

    public ApiResponse delete(Long id) {
        appTokenRepository.delete(id);
        return new ApiResponse("Токен успешно удалён");
    }

    public ApiResponse update(TokenDTO dto) {
        AppToken token = new AppToken(dto);
        appTokenRepository.save(token);
        return new ApiResponse("Токен успешно обновлён");
    }

    public ApiResponse create(TokenDTO dto) {
        AppToken token = new AppToken(dto);
        appTokenRepository.save(token);
        return new ApiResponse("Токен успешно создан");
    }

}
