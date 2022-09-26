package com.acme.user.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String username;
    private final String passwordHash;
    private final String refreshToken;
    private final UserRole role;
    private final long version;

    public User(UUID id, String username, String passwordHash, String refreshToken, UserRole role, long version) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.refreshToken = refreshToken;
        this.role = role;
        this.version = version;
    }

    public static User create(CreateUserPayload createUserPayload) {
        return new User(UUID.randomUUID(), createUserPayload.username, createUserPayload.passwordHash, "", createUserPayload.role, 1);
    }

    public UserAuthInfo getCredentials() {
        return new UserAuthInfo(id, username, passwordHash, role);
    }

    public record CreateUserPayload(String username, String passwordHash, UserRole role) {
    }

    public record UserAuthInfo(UUID userId, String username, String passwordHash, UserRole role) {
    }
}
