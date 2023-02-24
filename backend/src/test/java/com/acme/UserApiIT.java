package com.acme;

import com.acme.testsupport.containers.PostgresSharedContainerExtension;
import com.acme.testsupport.containers.RedisSharedContainerExtension;
import com.acme.user.api.UserApi.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({PostgresSharedContainerExtension.class, RedisSharedContainerExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserApiIT {
    @Autowired
    TestRestTemplate restTemplate;
    TokenPairDto currentTokenPair;

    @BeforeAll
    public void setup() {
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler());
    }

    @Test
    @Order(1)
    void shouldRegisterASingleTimeWithAUsernameAndLogin() {
        ResponseEntity<Void> response =
                restTemplate.postForEntity("/users", new CreateUserRequestDto("buyer", "secretpass", "buyer"), Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertThrows(HttpClientErrorException.Conflict.class,
                () -> restTemplate.postForEntity("/users", new CreateUserRequestDto("buyer", "secretpass", "buyer"), Void.class));

        restTemplate.postForEntity("/users", new CreateUserRequestDto("another_buyer", "secretpass", "buyer"), Void.class);
    }

    @Test
    @Order(2)
    void shouldGetAccessAndRefreshTokenOnLogin() {
        ResponseEntity<LoginResponseDto> loginResponseEntity =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponseEntity.getStatusCode());
        assertNotNull(loginResponseEntity.getBody());

        LoginResponseDto loginResponse = loginResponseEntity.getBody();
        assertNotNull(loginResponse.refreshToken());
        assertNotNull(loginResponse.accessToken());
        currentTokenPair = new TokenPairDto(loginResponse.accessToken(), loginResponse.refreshToken());
    }

    @Test
    @Order(2)
    void shouldGetNewAccessAndRefreshTokensOnRefresh() {
        ResponseEntity<TokenPairDto> loginResponseEntity =
                restTemplate.postForEntity("/users/refresh", new RefreshTokenDto(currentTokenPair.refreshToken()), TokenPairDto.class);
        assertEquals(HttpStatus.OK, loginResponseEntity.getStatusCode());
        assertNotNull(loginResponseEntity.getBody());

        assertNotNull(loginResponseEntity.getBody().refreshToken());
        assertNotNull(loginResponseEntity.getBody().accessToken());

        assertNotEquals(currentTokenPair.accessToken(), loginResponseEntity.getBody().accessToken());
        assertNotEquals(currentTokenPair.refreshToken(), loginResponseEntity.getBody().refreshToken());

        currentTokenPair = loginResponseEntity.getBody();
    }

    @Test
    @Order(3)
    void shouldNotAllowRefreshingAgainWithAlreadyUsedRefreshToken() {
        ResponseEntity<TokenPairDto> refreshResponseEntity =
                restTemplate.postForEntity("/users/refresh", new RefreshTokenDto(currentTokenPair.refreshToken()), TokenPairDto.class);

        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> restTemplate.postForEntity("/users/refresh", new RefreshTokenDto(currentTokenPair.refreshToken()), TokenPairDto.class)
        );

        assertEquals(HttpStatus.OK, refreshResponseEntity.getStatusCode());
        assertNotNull(refreshResponseEntity.getBody());
        assertNotNull(refreshResponseEntity.getBody().refreshToken());
        assertNotNull(refreshResponseEntity.getBody().accessToken());

        assertNotEquals(currentTokenPair.accessToken(), refreshResponseEntity.getBody().accessToken());
        assertNotEquals(currentTokenPair.refreshToken(), refreshResponseEntity.getBody().refreshToken());

        currentTokenPair = refreshResponseEntity.getBody();
    }

    @Test
    @Order(4)
    void shouldNoLongerBeAbleToUseRefreshTokenAfterLogout() {
        ResponseEntity<Void> logoutResponseEntity =
                restTemplate.postForEntity("/users/logout", new RefreshTokenDto(currentTokenPair.refreshToken()), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, logoutResponseEntity.getStatusCode());

        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> restTemplate.postForEntity("/users/refresh", new RefreshTokenDto(currentTokenPair.refreshToken()), TokenPairDto.class)
        );
    }

    @Test
    @Order(5)
    void shouldIncrementTheActiveSessionCountOnLogin() {
        ResponseEntity<LoginResponseDto> loginResponse1 =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponse1.getStatusCode());
        assertNotNull(loginResponse1.getBody());
        assertEquals(1, loginResponse1.getBody().numActiveSessions());

        ResponseEntity<LoginResponseDto> loginResponse2 =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponse2.getStatusCode());
        assertNotNull(loginResponse2.getBody());
        assertEquals(2, loginResponse2.getBody().numActiveSessions());

        ResponseEntity<LoginResponseDto> loginResponse3 =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponse3.getStatusCode());
        assertNotNull(loginResponse3.getBody());
        assertEquals(3, loginResponse3.getBody().numActiveSessions());
    }

    @Test
    @Order(6)
    void shouldDecrementTheActiveSessionCountOnLogout() {
        ResponseEntity<LoginResponseDto> loginResponse1 =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponse1.getStatusCode());
        assertNotNull(loginResponse1.getBody());
        assertEquals(4, loginResponse1.getBody().numActiveSessions());

        ResponseEntity<Void> logoutResponse = restTemplate.postForEntity("/users/logout", new RefreshTokenDto(loginResponse1.getBody().refreshToken()), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, logoutResponse.getStatusCode());

        ResponseEntity<LoginResponseDto> loginResponse2 =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponse2.getStatusCode());
        assertNotNull(loginResponse2.getBody());
        assertEquals(4, loginResponse2.getBody().numActiveSessions());

    }

    @Test
    @Order(7)
    void shouldDecrementToZeroTheActiveSessionCountOnLogoutall() {
        ResponseEntity<LoginResponseDto> loginResponse1 =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponse1.getStatusCode());
        assertNotNull(loginResponse1.getBody());
        assertEquals(5, loginResponse1.getBody().numActiveSessions());

        ResponseEntity<Void> logoutResponse = restTemplate.postForEntity("/users/logout/all", new RefreshTokenDto(loginResponse1.getBody().refreshToken()), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, logoutResponse.getStatusCode());

        ResponseEntity<LoginResponseDto> loginResponse2 =
                restTemplate.postForEntity("/users/login", new LoginRequestDto("buyer", "secretpass"), LoginResponseDto.class);
        assertEquals(HttpStatus.OK, loginResponse2.getStatusCode());
        assertNotNull(loginResponse2.getBody());
        assertEquals(1, loginResponse2.getBody().numActiveSessions());

    }


}
