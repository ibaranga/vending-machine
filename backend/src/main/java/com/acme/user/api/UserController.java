package com.acme.user.api;

import com.acme.user.application.UserApiUseCases;
import com.acme.user.domain.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Validated
public class UserController implements UserApi {
    private final UserApiUseCases userApiUseCases;

    public UserController(UserApiUseCases userApiUseCases) {
        this.userApiUseCases = userApiUseCases;
    }

    @Override
    public ResponseEntity<Void> register(CreateUserRequestDto createUserRequestDto) {
        UUID userId = userApiUseCases.register(
                createUserRequestDto.username(),
                createUserRequestDto.password(),
                UserRole.fromId(createUserRequestDto.role())
        );

        return ResponseEntity.created(URI.create("/%s".formatted(userId))).build();
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(UserApiAdapter.adapt(
                userApiUseCases.login(loginRequestDto.username(), loginRequestDto.password())
        ));
    }

    @Override
    public ResponseEntity<TokenPairDto> refresh(@Valid RefreshTokenDto loginDto) {
        return ResponseEntity.ok(UserApiAdapter.adapt(userApiUseCases.refresh(loginDto.refreshToken())));
    }

    @Override
    public ResponseEntity<Void> logout(@Valid RefreshTokenDto request) {
        userApiUseCases.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> logoutAll(@Valid RefreshTokenDto request) {
        userApiUseCases.logoutAll(request.refreshToken());
        return ResponseEntity.noContent().build();

    }
}
