package com.acme.user.infrastructure;

import com.acme.common.domain.ServiceException.ConflictException;
import com.acme.user.domain.User;
import org.springframework.dao.DataIntegrityViolationException;

public class UserEntityAdapter {
    public static User adapt(UserEntity userEntity) {
        return new User(userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPasswordHash(),
                userEntity.getRefreshToken(),
                userEntity.getRole(),
                userEntity.getVersion());
    }

    public static UserEntity adapt(User user) {
        return new UserEntity(user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getRefreshToken(),
                user.getRole(),
                user.getVersion(),
                null,
                null);
    }

    public static RuntimeException adapt(DataIntegrityViolationException e) {
        return new ConflictException();
    }
}

