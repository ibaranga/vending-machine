package com.acme.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    UUID save(User user);
}
