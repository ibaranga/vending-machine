package com.acme.testsupport.apiclient;

import com.acme.product.api.ProductApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
                "/product?page={page}&size={size}",
                GetProductsResponseDto.class, page, size
        );
    }

    @Override
    public ResponseEntity<GetProductsResponseDto> getSellerProducts(@Valid @Min(0) int page, @Valid @Max(100) int size) {
        return restTemplate.getForEntity(
                "/product/seller?page={page}&size={size}",
                GetProductsResponseDto.class, page, size
        );
    }

    @Override
    public ResponseEntity<Void> createProduct(CreateProductDto createProductDto) {
        return restTemplate.postForEntity("/product", requireNonNull(createProductDto), Void.class);
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(UUID productId) {
        return restTemplate.getForEntity("/product/{productId}", ProductDto.class, requireNonNull(productId));
    }

    @Override
    public void updateProduct(UUID productId, UpdateProductDto updateProductDto) {
        restTemplate.put("/product/{productId}", requireNonNull(updateProductDto), requireNonNull(productId));
    }

    @Override
    public void deleteProduct(UUID productId) {
        restTemplate.delete("/product/{productId}", requireNonNull(productId));
    }

}
