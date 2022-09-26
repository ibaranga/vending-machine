package com.acme;

import com.acme.product.api.ProductApi.CreateProductDto;
import com.acme.product.api.ProductApi.GetProductsResponseDto;
import com.acme.product.api.ProductApi.ProductDto;
import com.acme.product.api.ProductApi.UpdateProductDto;
import com.acme.testsupport.apiclient.ApiClient;
import com.acme.testsupport.containers.PostgresSharedContainerExtension;
import com.acme.testsupport.containers.RedisSharedContainerExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@Nested
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({PostgresSharedContainerExtension.class, RedisSharedContainerExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductApiIT {
    @Autowired
    TestRestTemplate testRestTemplate;

    ApiClient client;
    ApiClient clientAsSeller;
    ApiClient clientAsAnotherSeller;
    ApiClient clientAsBuyer;

    @BeforeAll
    public void setup() {
        client = ApiClient.create(testRestTemplate);
        clientAsBuyer = client.asBuyer("buyer_" + System.currentTimeMillis());
        clientAsSeller = client.asSeller("seller_" + System.currentTimeMillis());
        clientAsAnotherSeller = client.asSeller("another_seller" + System.currentTimeMillis());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WhenUserIsAuthenticatedAsSeller {
        // given
        CreateProductDto createProductDto = new CreateProductDto("Cola", 3, new BigDecimal("2.50"));
        UpdateProductDto updateProductDto = new UpdateProductDto("Cola zero", 2, new BigDecimal("2.30"));
        UUID testProductId;

        @Test
        @Order(1)
        void shouldBeAbleToCreateProducts() {
            // when
            ResponseEntity<Void> createProductResponse = clientAsSeller.product().createProduct(createProductDto);

            // then
            assertEquals(HttpStatus.CREATED, createProductResponse.getStatusCode());
            testProductId = client.parseIdFromLocationHeader(createProductResponse);
            assertNotNull(testProductId);
        }

        @Test
        @Order(2)
        void shouldBeAbleToReadASingleProduct() {
            // when
            ResponseEntity<ProductDto> getProductResponse = clientAsSeller.product().getProduct(testProductId);

            // then
            assertEquals(HttpStatus.OK, getProductResponse.getStatusCode());
            ProductDto product = getProductResponse.getBody();
            assertNotNull(product);

            assertEquals(createProductDto.productName(), product.productName());
            assertEquals(createProductDto.amountAvailable(), product.amountAvailable());
            assertEquals(createProductDto.cost(), product.cost());
        }

        @Test
        @Order(3)
        void shouldBeAbleToReadAListOfProducts() {
            // when
            ResponseEntity<GetProductsResponseDto> getProductsResponse = clientAsSeller.product().getSellerProducts(0, 10);
            assertEquals(HttpStatus.OK, getProductsResponse.getStatusCode());

            // then
            assertNotNull(getProductsResponse.getBody());
            List<ProductDto> products = getProductsResponse.getBody().products();
            assertFalse(products.isEmpty());
        }

        @Test
        @Order(4)
        void shouldBeAbleToUpdateProducts() {
            // when
            clientAsSeller.product().updateProduct(testProductId, updateProductDto);
            ResponseEntity<ProductDto> getProductResponse = clientAsSeller.product().getProduct(testProductId);
            ProductDto product = getProductResponse.getBody();

            // then
            assertNotNull(product);
            assertEquals(updateProductDto.productName(), product.productName());
            assertEquals(updateProductDto.amountAvailable(), product.amountAvailable());
            assertEquals(updateProductDto.cost(), product.cost());
        }

        @Test
        @Order(5)
        void shouldBeAbleToDeleteProducts() {
            // when
            clientAsSeller.product().deleteProduct(testProductId);

            // then
            assertThrows(HttpClientErrorException.NotFound.class,
                    // when
                    () -> clientAsSeller.product().getProduct(testProductId));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WhenUserIsNotAuthenticatedAsSeller {
        UUID testProductId;

        Stream<ApiClient> apiClientsNotAuthenticatedAsSeller() {
            return Stream.of(client, clientAsBuyer);
        }

        Stream<ApiClient> apiClientsNotAuthenticatedAsSellerOwningTestProduct() {
            return Stream.of(client, clientAsBuyer, clientAsAnotherSeller);
        }

        @PostConstruct
        void setup() {
            testProductId = clientAsSeller.parseIdFromLocationHeader(clientAsSeller
                    .product()
                    .createProduct(new CreateProductDto("Random Product", 1, new BigDecimal("2.34"))));
        }

        @ParameterizedTest
        @MethodSource("apiClientsNotAuthenticatedAsSeller")
        void shouldBeAbleToGetProducts(ApiClient client) {
            // when
            ResponseEntity<GetProductsResponseDto> products = client.product().getProducts(0, 100);

            // then
            assertEquals(HttpStatus.OK, products.getStatusCode());
            assertNotNull(products.getBody());
            assertEquals(1, products.getBody().products().size());
            ProductDto product = products.getBody().products().get(0);

            assertEquals("Random Product", product.productName());
            assertEquals(1, product.amountAvailable());
            assertEquals(new BigDecimal("2.34"), product.cost());
        }

        @ParameterizedTest
        @MethodSource("apiClientsNotAuthenticatedAsSeller")
        void shouldNotBeAbleToCreateProducts(ApiClient client) {
            // given
            CreateProductDto createProductDto = new CreateProductDto("cola", 3, new BigDecimal("2.50"));

            // then
            assertThrows(
                    Forbidden.class,
                    // when
                    () -> client.product().createProduct(createProductDto));
        }

        @ParameterizedTest
        @MethodSource("apiClientsNotAuthenticatedAsSellerOwningTestProduct")
        void shouldNotBeAbleToUpdateProducts(ApiClient client) {
            // given
            UpdateProductDto updateProductDto = new UpdateProductDto(null, 2, null);

            // then
            assertThrows(
                    Forbidden.class,
                    // when
                    () -> client.product().updateProduct(testProductId, updateProductDto));
        }

        @ParameterizedTest
        @MethodSource("apiClientsNotAuthenticatedAsSellerOwningTestProduct")
        void shouldNotBeAbleToDeleteProducts(ApiClient client) {
            // then
            assertThrows(
                    Forbidden.class,
                    // when
                    () -> client.product().deleteProduct(testProductId));
        }
    }


}
