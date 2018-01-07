package com.art.config;

import com.art.model.PersistentLogin;
import com.art.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository("TokenRepository")
@Transactional
public class HibernateTokenRepositoryImpl implements PersistentTokenRepository {

    @Autowired
    private TokenRepository tokenRepository;
    //static final Logger logger = LoggerFactory.getLogger(HibernateTokenRepositoryImpl.class);



    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        //logger.info("Creating Token for user : {}", token.getUsername());
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());
        tokenRepository.saveAndFlush(persistentLogin);
    }


    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        //logger.info("Fetch Token if any for seriesId : {}", seriesId);
        try {
            PersistentLogin persistentLogin = tokenRepository.findBySeries(seriesId);
            return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
                    persistentLogin.getToken(), persistentLogin.getLast_used());
        } catch (Exception e) {
            //logger.info("Token not found...");
            return null;
        }
    }

    @Override
    public void removeUserTokens(String username) {
        //logger.info("Removing Token if any for user : {}", username);
        PersistentLogin persistentLogin = tokenRepository.findByUsername(username);
        if (persistentLogin != null) {
            //logger.info("rememberMe was selected");
            tokenRepository.delete(persistentLogin);
        }

    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
        //logger.info("Updating Token for seriesId : {}", seriesId);
        PersistentLogin persistentLogin = tokenRepository.findBySeries(seriesId);
        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);
        tokenRepository.saveAndFlush(persistentLogin);
    }

}
