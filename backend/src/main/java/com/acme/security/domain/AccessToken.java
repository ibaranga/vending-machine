package com.acme.security.domain;

import java.time.Instant;
import java.util.UUID;

public record AccessToken(UUID id, UUID principalId, String value, Instant expiresAt, String role) {
}
