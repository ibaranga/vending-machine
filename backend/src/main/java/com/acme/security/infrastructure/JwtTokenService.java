package com.acme.security.infrastructure;

import com.acme.security.domain.AccessToken;
import com.acme.security.domain.RefreshToken;
import com.acme.security.domain.TokenPair;
import com.acme.security.domain.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenService implements TokenService {
    private final Clock clock;
    private final String accessTokenSecret;
    private final Duration accessTokenDuration;
    private final String refreshTokenSecret;
    private final Duration refreshTokenDuration;

    public JwtTokenService(Clock clock,
                           @Value("${security.jwt.access_token.secret}") String accessTokenSecret,
                           @Value("${security.jwt.access_token.duration}") Duration accessTokenDuration,
                           @Value("${security.jwt.refresh_token.secret}") String refreshTokenSecret,
                           @Value("${security.jwt.refresh_token.duration}") Duration refreshTokenDuration
    ) {
        this.clock = clock;
        this.accessTokenSecret = accessTokenSecret;
        this.accessTokenDuration = accessTokenDuration;
        this.refreshTokenSecret = refreshTokenSecret;
        this.refreshTokenDuration = refreshTokenDuration;
    }

    @Override
    public TokenPair generateTokenPair(UUID principalId, String role) {
        return new TokenPair(generateAccessToken(principalId, role), generateRefreshToken(principalId, role));
    }

    public AccessToken generateAccessToken(UUID principalId, String role) {
        UUID tokenId = UUID.randomUUID();
        Instant issuedAt = clock.instant().truncatedTo(ChronoUnit.SECONDS);
        Instant expiresAt = issuedAt.plus(accessTokenDuration);

        String token = JWT.create()
                .withSubject(principalId.toString())
                .withIssuer("acme")
                .withJWTId(tokenId.toString())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(accessTokenSecret));

        return new AccessToken(tokenId, principalId, token, expiresAt, role);
    }

    public RefreshToken generateRefreshToken(UUID principalId, String role) {
        UUID tokenId = UUID.randomUUID();
        Instant issuedAt = clock.instant().truncatedTo(ChronoUnit.SECONDS);
        Instant expiresAt = issuedAt.plus(refreshTokenDuration);

        String token = JWT.create()
                .withSubject(principalId.toString())
                .withIssuer("acme")
                .withJWTId(tokenId.toString())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(refreshTokenSecret));

        return new RefreshToken(tokenId, principalId, token, expiresAt, role);
    }

    public Optional<AccessToken> parseAccessToken(String rawToken) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(accessTokenSecret)).build().verify(rawToken);
            if (decodedJWT.getExpiresAtAsInstant().isBefore(clock.instant())) {
                return Optional.empty();
            }
            return Optional.of(new AccessToken(
                    UUID.fromString(decodedJWT.getId()),
                    UUID.fromString(decodedJWT.getSubject()),
                    rawToken,
                    decodedJWT.getExpiresAtAsInstant(),
                    decodedJWT.getClaim("role").asString())
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<RefreshToken> parseRefreshToken(String rawToken) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(refreshTokenSecret)).build().verify(rawToken);
            if (decodedJWT.getExpiresAtAsInstant().isBefore(clock.instant())) {
                return Optional.empty();
            }
            return Optional.of(new RefreshToken(
                    UUID.fromString(decodedJWT.getId()),
                    UUID.fromString(decodedJWT.getSubject()),
                    rawToken,
                    decodedJWT.getExpiresAtAsInstant(),
                    decodedJWT.getClaim("role").asString()
            ));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
