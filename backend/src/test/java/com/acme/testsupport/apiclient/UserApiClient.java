package com.acme.testsupport.apiclient;

import com.acme.user.api.UserApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

class UserApiClient implements UserApi {
    private final RestTemplate restTemplate;

    public UserApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<Void> register(CreateUserRequestDto createUserRequestDto) {
        return restTemplate.postForEntity("/user", createUserRequestDto, Void.class);
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        return restTemplate.postForEntity("/user/login", loginRequestDto, LoginResponseDto.class);
    }

    @Override
    public ResponseEntity<TokenPairDto> refresh(@Valid RefreshTokenDto request) {
        return restTemplate.postForEntity("/user/refresh", request, TokenPairDto.class);
    }

    @Override
    public ResponseEntity<Void> logout(@Valid RefreshTokenDto request) {
        return restTemplate.postForEntity("/user/logout", request, Void.class);
    }

    @Override
    public ResponseEntity<Void> logoutAll(@Valid RefreshTokenDto request) {
        return restTemplate.postForEntity("/user/logoutall", request, Void.class);
    }
}
