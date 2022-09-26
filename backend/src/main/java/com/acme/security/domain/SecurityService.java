package com.acme.security.domain;


import com.acme.common.domain.ServiceException;
import com.acme.user.domain.User.UserAuthInfo;

public class SecurityService {
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityService(TokenService tokenService, TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean passwordMatches(String plainTextPassword, String encodedPassword) {
        return passwordEncoder.matches(plainTextPassword, encodedPassword);
    }

    public String encodePassword(String plainTextPassword) {
        return passwordEncoder.encode(plainTextPassword);
    }

    public LoginResult login(UserAuthInfo userAuthInfo) {
        TokenPair tokenPair = tokenService.generateTokenPair(userAuthInfo.userId(), userAuthInfo.role().getId());
        tokenRepository.save(tokenPair.refreshToken());
        return new LoginResult(tokenPair.accessToken(), tokenPair.refreshToken(), tokenRepository.countTokens(userAuthInfo.userId()));
    }

    public TokenPair refresh(String rawRefreshToken) {
        RefreshToken refreshToken = this.tokenService.parseRefreshToken(rawRefreshToken)
                .orElseThrow(() -> new ServiceException.Unauthorized("Invalid refresh token for refresh"));

        if (!tokenRepository.invalidate(refreshToken)) {
            throw new ServiceException.Unauthorized("Could not match refresh token");
        }
        TokenPair tokenPair = tokenService.generateTokenPair(refreshToken.principalId(), refreshToken.role());
        tokenRepository.save(tokenPair.refreshToken());
        return tokenPair;
    }

    public void logout(String rawRefreshToken) {
        RefreshToken refreshToken = this.tokenService.parseRefreshToken(rawRefreshToken)
                .orElseThrow(() -> new ServiceException.Unauthorized("Invalid refresh token for logout"));

        if (!tokenRepository.invalidate(refreshToken)) {
            throw new ServiceException.Unauthorized("Could not match refresh token");
        }
    }

    public void logoutAll(String rawRefreshToken) {
        RefreshToken refreshToken = this.tokenService.parseRefreshToken(rawRefreshToken)
                .orElseThrow(() -> new ServiceException.Unauthorized("Invalid refresh token for logoutall"));
        if (!tokenRepository.invalidateAll(refreshToken.principalId())) {
            throw new ServiceException.Unauthorized("Could not match refresh token");
        }
    }

    public record LoginResult(AccessToken accessToken, RefreshToken refreshToken, long numActiveSessions) {
    }

}
