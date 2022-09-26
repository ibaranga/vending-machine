package com.acme.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum UserRole {
    SELLER("seller"),
    BUYER("buyer");

    private static final Map<String, UserRole> rolesById = Stream.of(UserRole.values())
            .collect(Collectors.toMap(UserRole::getId, Function.identity()));

    private final String id;

    public static UserRole fromId(String id) {
        return UserRole.rolesById.get(id);
    }
}
