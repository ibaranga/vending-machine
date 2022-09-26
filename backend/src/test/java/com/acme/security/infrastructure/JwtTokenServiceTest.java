package com.acme.security.infrastructure;

import com.acme.security.domain.AccessToken;
import com.acme.security.domain.RefreshToken;
import com.acme.security.domain.TokenPair;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtTokenServiceTest {
    // given
    UUID principalId = UUID.randomUUID();
    Duration accessTokenDuration = Duration.ofMinutes(10);
    Duration refreshTokenDuration = Duration.ofDays(10);

    @Test
    void generateTokenPair() {
        // given
        Clock clock = Clock.systemDefaultZone();
        JwtTokenService jwtTokenService = new JwtTokenService(
                clock,
                "secret",
                accessTokenDuration,
                "secret",
                refreshTokenDuration);

        // when
        TokenPair generatedTokenPair = jwtTokenService.generateTokenPair(principalId, "admin");
        // then
        assertNotNull(generatedTokenPair);
        assertNotNull(generatedTokenPair.accessToken());
        assertNotNull(generatedTokenPair.refreshToken());

        assertValidAccessTokenAtClock(true, generatedTokenPair.accessToken(), clock);
        assertValidAccessTokenAtClock(true, generatedTokenPair.accessToken(), Clock.offset(clock, accessTokenDuration.dividedBy(2)));
        assertValidAccessTokenAtClock(false, generatedTokenPair.accessToken(), Clock.offset(clock, accessTokenDuration.plus(Duration.ofSeconds(1))));
        assertValidAccessTokenAtClock(false, generatedTokenPair.accessToken(), Clock.offset(clock, accessTokenDuration.plus(Duration.ofDays(1))));

        assertValidRefreshTokenAtClock(true, generatedTokenPair.refreshToken(), clock);
        assertValidRefreshTokenAtClock(true, generatedTokenPair.refreshToken(), Clock.offset(clock, refreshTokenDuration.dividedBy(2)));
        assertValidRefreshTokenAtClock(false, generatedTokenPair.refreshToken(), Clock.offset(clock, refreshTokenDuration.plus(Duration.ofSeconds(1))));
        assertValidRefreshTokenAtClock(false, generatedTokenPair.refreshToken(), Clock.offset(clock, refreshTokenDuration.plus(Duration.ofDays(1))));
    }

    void assertValidAccessTokenAtClock(boolean isExpectedValid, AccessToken accessToken, Clock clock) {
        JwtTokenService jwtTokenService = new JwtTokenService(
                clock,
                "secret",
                accessTokenDuration,
                "secret",
                refreshTokenDuration);
        Optional<AccessToken> parsedAccessTokenOptional = jwtTokenService.parseAccessToken(accessToken.value());
        assertEquals(isExpectedValid, parsedAccessTokenOptional.isPresent());
        parsedAccessTokenOptional.ifPresent(parsedAccessToken -> assertEquals(accessToken, parsedAccessToken));
    }

    void assertValidRefreshTokenAtClock(boolean isExpectedValid, RefreshToken refreshToken, Clock clock) {
        JwtTokenService jwtTokenService = new JwtTokenService(
                clock,
                "secret",
                accessTokenDuration,
                "secret",
                refreshTokenDuration);
        Optional<RefreshToken> parsedAccessTokenOptional = jwtTokenService.parseRefreshToken(refreshToken.value());
        assertEquals(isExpectedValid, parsedAccessTokenOptional.isPresent());
        parsedAccessTokenOptional.ifPresent(parsedAccessToken -> assertEquals(refreshToken, parsedAccessToken));
    }

}