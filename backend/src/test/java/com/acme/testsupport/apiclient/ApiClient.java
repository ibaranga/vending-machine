package com.acme.testsupport.apiclient;

import com.acme.product.api.ProductApi;
import com.acme.user.api.UserApi;
import com.acme.user.api.UserApi.CreateUserRequestDto;
import com.acme.user.domain.UserRole;
import com.acme.vendingmachine.api.VendingMachineApi;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiClient {
    private final RestTemplate restTemplate;
    private final ProductApiClient productApiClient;
    private final UserApiClient userApiClient;
    private final VendingMachineApiClient vendingMachineApiClient;

    private ApiClient(RestTemplate restTemplate, ProductApiClient productApiClient, UserApiClient userApiClient, VendingMachineApiClient vendingMachineApiClient) {
        this.restTemplate = restTemplate;
        this.productApiClient = productApiClient;
        this.userApiClient = userApiClient;
        this.vendingMachineApiClient = vendingMachineApiClient;
    }

    public static ApiClient create(TestRestTemplate testRestTemplate) {
        RestTemplate restTemplate = testRestTemplate.getRestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return create(restTemplate);
    }

    public static ApiClient create(RestTemplate restTemplate) {
        return new ApiClient(
                restTemplate,
                new ProductApiClient(restTemplate),
                new UserApiClient(restTemplate),
                new VendingMachineApiClient(restTemplate)
        );
    }

    public ProductApi product() {
        return this.productApiClient;
    }

    public UserApi user() {
        return this.userApiClient;
    }

    public VendingMachineApi vendingMachine() {
        return this.vendingMachineApiClient;
    }

    public ApiClient asBuyer(String username) {
        return asUser(username, UserRole.BUYER);
    }

    public ApiClient asSeller(String username) {
        return asUser(username, UserRole.SELLER);
    }

    public UUID parseIdFromLocationHeader(ResponseEntity<?> response) {
        URI location = requireNonNull(response).getHeaders().getLocation();
        if (location == null) {
            return null;
        }
        return UUID.fromString(location.getPath().substring(1 + location.getPath().lastIndexOf("/")));
    }

    private ApiClient asUser(String username, UserRole role) {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(
                requireNonNull(username),
                "secretpass",
                requireNonNull(role).getId()
        );
        ResponseEntity<Void> response = user().register(createUserRequestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<UserApi.LoginResponseDto> loginResponse = userApiClient.login(new UserApi.LoginRequestDto(createUserRequestDto.username(), createUserRequestDto.password()));
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        RestTemplate restTemplateWithBasicAuth = new RestTemplateBuilder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(loginResponse.getBody().accessToken()))
                .errorHandler(restTemplate.getErrorHandler())
                .uriTemplateHandler(restTemplate.getUriTemplateHandler())
                .build();

        return create(restTemplateWithBasicAuth);
    }


}
