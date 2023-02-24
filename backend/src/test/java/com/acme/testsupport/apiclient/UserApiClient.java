package com.acme.testsupport.apiclient;

import com.acme.user.api.UserApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class UserApiClient implements UserApi {
    private final RestTemplate restTemplate;

    public UserApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<Void> register(CreateUserRequestDto createUserRequestDto) {
        return restTemplate.postForEntity("/users", createUserRequestDto, Void.class);
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        return restTemplate.postForEntity("/users/login", loginRequestDto, LoginResponseDto.class);
    }

    @Override
    public ResponseEntity<TokenPairDto> refresh(@Valid RefreshTokenDto request) {
        return restTemplate.postForEntity("/users/refresh", request, TokenPairDto.class);
    }

    @Override
    public ResponseEntity<Void> logout(@Valid RefreshTokenDto request) {
        return restTemplate.postForEntity("/users/logout", request, Void.class);
    }

    @Override
    public ResponseEntity<Void> logoutAll(@Valid RefreshTokenDto request) {
        return restTemplate.postForEntity("/users/logoutall", request, Void.class);
    }
}
