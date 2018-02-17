package com.art.func;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class IsRememberMeAuthenticatedFunc {

    private final SecurityContextHolder securityContextHolder;
    private boolean isRememberMe;

    @Autowired
    public IsRememberMeAuthenticatedFunc(SecurityContextHolder securityContextHolder) {
        this.securityContextHolder = securityContextHolder;
    }

    private boolean isRememberMeAuthenticated(SecurityContextHolder securityContextHolder) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

    public boolean isRememberMe() {
        return isRememberMeAuthenticated(securityContextHolder);
    }

    public void setRememberMe(boolean rememberMe) {
        isRememberMe = rememberMe;
    }
}
