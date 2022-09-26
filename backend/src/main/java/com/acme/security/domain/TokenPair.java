package com.acme.security.domain;

public record TokenPair(AccessToken accessToken, RefreshToken refreshToken) {
}
