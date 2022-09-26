package com.acme.user.application;

import com.acme.common.domain.ServiceException;
import com.acme.security.domain.SecurityService;
import com.acme.security.domain.SecurityService.LoginResult;
import com.acme.security.domain.TokenPair;
import com.acme.user.domain.User.CreateUserPayload;
import com.acme.user.domain.User.UserAuthInfo;
import com.acme.user.domain.UserRole;
import com.acme.user.domain.UserService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserApiUseCases {
    private final UserService userService;
    private final SecurityService securityService;

    public UserApiUseCases(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    public LoginResult login(String username, String password) {
        UserAuthInfo authInfo = userService.getUserAuthInfo(username)
                .filter(userAuthInfo -> securityService.passwordMatches(password, userAuthInfo.passwordHash()))
                .orElseThrow(ServiceException.Unauthorized::new);

        return securityService.login(authInfo);
    }

    public UUID register(String username, String password, UserRole role) {
        return userService.register(new CreateUserPayload(username, securityService.encodePassword(password), role));
    }

    public TokenPair refresh(String refreshToken) {
        return securityService.refresh(refreshToken);
    }

    public void logout(String refreshToken) {
        securityService.logout(refreshToken);
    }

    public void logoutAll(String refreshToken) {
        securityService.logoutAll(refreshToken);
    }
}
