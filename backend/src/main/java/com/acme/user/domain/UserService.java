package com.acme.user.domain;

import com.acme.user.domain.User.CreateUserPayload;
import com.acme.user.domain.User.UserAuthInfo;

import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID register(CreateUserPayload createUserPayload) {
        User user = User.create(createUserPayload);
        return userRepository.save(user);
    }

    public Optional<UserAuthInfo> getUserAuthInfo(String username) {
        return userRepository.findByUsername(username)
                .map(User::getCredentials);
    }

}
