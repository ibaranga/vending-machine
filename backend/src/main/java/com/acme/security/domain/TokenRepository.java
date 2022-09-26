package com.acme.security.domain;

import java.util.UUID;

public interface TokenRepository {

    void save(RefreshToken refreshToken);

    boolean invalidate(RefreshToken refreshToken);

    boolean invalidateAll(UUID principalId);

    long countTokens(UUID principalId);

}
