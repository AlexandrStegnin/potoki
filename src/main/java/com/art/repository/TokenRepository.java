package com.art.repository;

import com.art.model.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<PersistentLogin, String> {

    PersistentLogin findBySeries(String series);
    PersistentLogin findByUsername(String username);
    PersistentLogin findByToken(String token);

}
