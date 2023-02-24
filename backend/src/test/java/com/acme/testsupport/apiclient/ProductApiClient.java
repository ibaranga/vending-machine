package com.acme.testsupport.apiclient;

import com.acme.product.api.ProductApi;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

class ProductApiClient implements ProductApi {
    private final RestTemplate restTemplate;

    public ProductApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<GetProductsResponseDto> getProducts(int page, int size) {
        return restTemplate.getForEntity(
                "/products?page={page}&size={size}",
                GetProductsResponseDto.class, page, size
        );
    }

    @Override
    public ResponseEntity<GetProductsResponseDto> getSellerProducts(@Valid @Min(0) int page, @Valid @Max(100) int size) {
        return restTemplate.getForEntity(
                "/products/seller?page={page}&size={size}",
                GetProductsResponseDto.class, page, size
        );
    }

    @Override
    public ResponseEntity<Void> createProduct(CreateProductDto createProductDto) {
        return restTemplate.postForEntity("/products", requireNonNull(createProductDto), Void.class);
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(UUID productId) {
        return restTemplate.getForEntity("/products/{productId}", ProductDto.class, requireNonNull(productId));
    }

    @Override
    public void updateProduct(UUID productId, UpdateProductDto updateProductDto) {
        restTemplate.put("/products/{productId}", requireNonNull(updateProductDto), requireNonNull(productId));
    }

    @Override
    public void deleteProduct(UUID productId) {
        restTemplate.delete("/products/{productId}", requireNonNull(productId));
    }

}
