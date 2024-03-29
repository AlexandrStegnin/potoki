package com.art.service;

import com.art.model.AppUser;
import com.art.model.PasswordResetToken;
import com.art.repository.PasswordResetTokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

@Service
@Transactional
@Repository
public class PasswordResetTokenService {

    @Resource(name = "passwordResetTokenRepository")
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetToken create(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.saveAndFlush(passwordResetToken);
    }

    public String validatePasswordResetToken(Long id, String token) {
        PasswordResetToken passToken =
                passwordResetTokenRepository.findByToken(token);
        if ((passToken == null) || (!Objects.equals(passToken.getAppUser()
                .getId(), id))) {
            return "invalidToken";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "expired";
        }

        AppUser user = passToken.getAppUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, Collections.singletonList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return "Valid";
    }

    public PasswordResetToken findById(Long id) {
        return passwordResetTokenRepository.findById(id);
    }

    public void deleteById(Long id) {
        passwordResetTokenRepository.deleteById(id);
    }

}
