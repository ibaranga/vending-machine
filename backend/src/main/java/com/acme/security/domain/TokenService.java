package com.acme.security.domain;

import java.util.Optional;
import java.util.UUID;

public interface TokenService {
    TokenPair generateTokenPair(UUID principalId, String role);

    Optional<AccessToken> parseAccessToken(String rawToken);

    Optional<RefreshToken> parseRefreshToken(String rawToken);

}
