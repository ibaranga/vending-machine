package com.acme.user.api;

import com.acme.security.domain.SecurityService.LoginResult;
import com.acme.security.domain.TokenPair;
import com.acme.user.api.UserApi.TokenPairDto;
import org.springframework.stereotype.Component;

import static com.acme.user.api.UserApi.LoginResponseDto;


@Component
public class UserApiAdapter {

    public static TokenPairDto adapt(TokenPair tokenPair) {
        return new TokenPairDto(
                tokenPair.accessToken().value(),
                tokenPair.refreshToken().value()
        );
    }

    public static LoginResponseDto adapt(LoginResult loginResult) {
        return new LoginResponseDto(
                loginResult.accessToken().value(),
                loginResult.refreshToken().value(),
                loginResult.numActiveSessions()
        );
    }
}
