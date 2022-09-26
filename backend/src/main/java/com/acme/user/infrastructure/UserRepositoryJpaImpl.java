package com.acme.user.infrastructure;

import com.acme.user.domain.User;
import com.acme.user.domain.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryJpaImpl implements UserRepository {
    private final Repository repository;

    public UserRepositoryJpaImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findOneByUsername(username).map(UserEntityAdapter::adapt);
    }

    @Override
    public UUID save(User user) {
        try {
            return repository.save(UserEntityAdapter.adapt(user)).getId();
        } catch (DataIntegrityViolationException e) {
            throw UserEntityAdapter.adapt(e);
        }
    }

    interface Repository extends JpaRepository<UserEntity, UUID> {
        Optional<UserEntity> findOneByUsername(String username);
    }

}
