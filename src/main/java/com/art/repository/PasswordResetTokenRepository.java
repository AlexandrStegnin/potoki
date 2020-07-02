package com.art.repository;

import com.art.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findById(Long id);
    void deleteById(Long id);
}
