package com.acme.user.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@PreAuthorize("permitAll()")
public interface UserApi {
    @PostMapping
    ResponseEntity<Void> register(@RequestBody @Valid CreateUserRequestDto createUserRequestDto);

    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto);

    @PostMapping("/refresh")
    ResponseEntity<TokenPairDto> refresh(@RequestBody @Valid RefreshTokenDto loginDto);

    @PostMapping("/logout")
    ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenDto request);

    @PostMapping("/logout/all")
    ResponseEntity<Void> logoutAll(@RequestBody @Valid RefreshTokenDto request);

    record CreateUserRequestDto(
            @NotNull @Size(min = 4, max = 64) String username,
            @Size(min = 8, max = 32) @NotNull String password,
            @NotNull @NotEmpty String role) {
    }

    record LoginRequestDto(
            @NotNull @Size(min = 4, max = 64) String username,
            @Size(min = 8, max = 32) @NotNull String password) {
    }

    record LoginResponseDto(
            @NotNull String accessToken,
            @NotNull String refreshToken,
            @NotNull Long numActiveSessions
    ) {

    }

    record TokenPairDto(
            @NotNull String accessToken,
            @NotNull String refreshToken) {
    }

    record RefreshTokenDto(@NotNull String refreshToken) {
    }


}