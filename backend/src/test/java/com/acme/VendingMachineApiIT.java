package com.acme;

import com.acme.product.api.ProductApi.CreateProductDto;
import com.acme.product.api.ProductApi.ProductDto;
import com.acme.testsupport.apiclient.ApiClient;
import com.acme.testsupport.containers.PostgresSharedContainerExtension;
import com.acme.testsupport.containers.RedisSharedContainerExtension;
import com.acme.vendingmachine.api.VendingMachineApi.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException.Conflict;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;


@Nested
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({PostgresSharedContainerExtension.class, RedisSharedContainerExtension.class})
class VendingMachineApiIT {
    @Autowired
    TestRestTemplate testRestTemplate;

    ApiClient client;
    ApiClient clientAsSeller;
    ApiClient clientAsAnotherSeller;
    ApiClient clientAsBuyer;

    // given
    ProductDto productAt_1_95;
    ProductDto productAt_1_45;
    ProductDto productAt_1_33_outOfStock;

    @BeforeAll
    public void setup() {
        client = ApiClient.create(testRestTemplate);
        clientAsBuyer = client.asBuyer("buyer_" + System.currentTimeMillis());
        clientAsSeller = client.asSeller("seller_" + System.currentTimeMillis());
        clientAsAnotherSeller = client.asSeller("another_seller" + System.currentTimeMillis());

        productAt_1_95 = createAndGetProduct("Cola", 4, new BigDecimal("1.95"));
        productAt_1_45 = createAndGetProduct("Fanta", 4, new BigDecimal("1.45"));
        productAt_1_33_outOfStock = createAndGetProduct("Sprite", 0, new BigDecimal("1.95"));

    }

    @Test
    @Order(1)
    void shouldNotFindBalanceWithoutDepositFirst() {
        assertThrows(NotFound.class, () -> clientAsBuyer.vendingMachine().getBalance());
    }

    @Test
    @Order(2)
    void shouldUpdateBalanceAfterDeposit() {
        // when
        deposit(ApiCoins.HUNDRED);

        // then
        assertEquals(new BigDecimal("1.00"), getBalance());

        // when
        deposit(ApiCoins.FIFTY);

        // then
        assertEquals(new BigDecimal("1.50"), getBalance());

        // when
        deposit(ApiCoins.TEN);
        // then
        assertEquals(new BigDecimal("1.60"), getBalance());

        // when
        deposit(ApiCoins.FIVE);
        // then
        assertEquals(new BigDecimal("1.65"), getBalance());
    }

    @Test
    @Order(3)
    void shouldNotBeAbleToBuyOutOfStockProductAndHaveUnmodifiedBalance() {
        // then
        assertThrows(Conflict.class, () -> clientAsBuyer.vendingMachine()
                // when
                .buy(new PurchaseRequestDto(productAt_1_33_outOfStock.id(), 1)));
        assertEquals(new BigDecimal("1.65"), getBalance());
    }


    @Test
    @Order(4)
    void shouldNotBeAbleToBuyWhenAttemptingToBuyWithInsufficientAmountAndHaveUnmodifiedBalance() {
        // then
        assertThrows(Conflict.class,
                // when
                () -> clientAsBuyer.vendingMachine().buy(new PurchaseRequestDto(productAt_1_95.id(), 1)));
        assertEquals(new BigDecimal("1.65"), getBalance());

        // when
        assertThrows(Conflict.class,
                // when
                () -> clientAsBuyer.vendingMachine().buy(new PurchaseRequestDto(productAt_1_45.id(), 2)));

        // then
        assertEquals(new BigDecimal("1.65"), getBalance());
    }

    @Test
    @Order(5)
    void shouldBuyAndGetChangeWhenBalanceIsSufficient() {
        // when
        ResponseEntity<PurchaseResponseDto> buyResponse = clientAsBuyer.vendingMachine()
                .buy(new PurchaseRequestDto(productAt_1_45.id(), 1));

        // then
        assertEquals(HttpStatus.OK, buyResponse.getStatusCode());
        assertNotNull(buyResponse.getBody());
        assertEquals(productAt_1_45.productName(), buyResponse.getBody().purchasedProductName());
        assertEquals(productAt_1_45.cost(), buyResponse.getBody().totalSpentAmount());
        assertArrayEquals(new ApiCoins[]{ApiCoins.TWENTY}, buyResponse.getBody().change());
        assertEquals(new BigDecimal("0.00"), getBalance());

    }

    @Test
    @Order(6)
    void shouldUpdateBalanceToZeroAfterReset() {
        // given
        deposit(ApiCoins.HUNDRED, ApiCoins.FIFTY, ApiCoins.FIVE);
        assertEquals(new BigDecimal("1.55"), getBalance());
        // when
        ResponseEntity<Void> resetResponse = clientAsBuyer.vendingMachine().reset();
        // then
        assertEquals(HttpStatus.NO_CONTENT, resetResponse.getStatusCode());
        assertEquals(new BigDecimal("0.00"), getBalance());
    }


    private BigDecimal getBalance() {
        ResponseEntity<GetBalanceResponseDto> balance = clientAsBuyer.vendingMachine().getBalance();
        assertEquals(HttpStatus.OK, balance.getStatusCode());
        assertNotNull(balance.getBody());
        return balance.getBody().balance();
    }

    ProductDto createAndGetProduct(String productName, int amountAvailable, BigDecimal cost) {
        ResponseEntity<Void> createResponse = clientAsSeller
                .product().createProduct(new CreateProductDto(productName, amountAvailable, cost));
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        UUID productId = clientAsSeller.parseIdFromLocationHeader(createResponse);
        return requireNonNull(clientAsSeller.product().getProduct(productId).getBody());
    }

    void deposit(ApiCoins... coins) {
        for (var coin : coins) {
            ResponseEntity<Void> depositResponse = clientAsBuyer.vendingMachine().deposit(new DepositRequestDto(coin));
            assertEquals(HttpStatus.NO_CONTENT, depositResponse.getStatusCode());
        }

    }

}
