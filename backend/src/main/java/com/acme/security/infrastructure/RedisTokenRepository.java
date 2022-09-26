package com.acme.security.infrastructure;

import com.acme.security.domain.RefreshToken;
import com.acme.security.domain.TokenRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

@Repository
public class RedisTokenRepository implements TokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForSet().add(getRefreshTokensSetKey(refreshToken.principalId()), refreshToken.id().toString());
    }

    @Override
    public boolean invalidate(RefreshToken refreshToken) {
        Long removedTokens = redisTemplate.opsForSet().remove(getRefreshTokensSetKey(refreshToken.principalId()), refreshToken.id().toString());
        return Objects.equals(1L, removedTokens);
    }

    @Override
    public boolean invalidateAll(UUID principalId) {
        return Boolean.TRUE.equals(redisTemplate.delete(getRefreshTokensSetKey(principalId)));
    }

    @Override
    public long countTokens(UUID principalId) {
        return redisTemplate.opsForSet().size(getRefreshTokensSetKey(principalId));
    }

    private String getRefreshTokensSetKey(UUID principalId) {
        return "rts:%s".formatted(principalId.toString());
    }


}
