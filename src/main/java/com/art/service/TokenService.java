package com.art.service;

import com.art.model.PersistentLogin;
import com.art.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Repository
public class TokenService {

    @Autowired
    TokenRepository tokenRepository;

    public List<PersistentLogin> findAll() {
        return tokenRepository.findAll();
    }

    public PersistentLogin findBySeries(String series){
        return tokenRepository.findBySeries(series);
    }

    public PersistentLogin findByUsername(String username){
        return tokenRepository.findByUsername(username);
    }

    public PersistentLogin findByToken(String token){
        return tokenRepository.findByToken(token);
    }
}
