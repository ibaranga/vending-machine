package com.acme.product.api;

import com.acme.common.api.SecurityHelper;
import com.acme.product.domain.ProductService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController implements ProductApi {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @PermitAll
    public ResponseEntity<GetProductsResponseDto> getProducts(int page, int size) {
        return ResponseEntity.ok(new GetProductsResponseDto(productService
                .findProducts(page, size)
                .stream()
                .map(ProductApiAdapter::adapt)
                .toList()
        ));
    }

    @Override
    public ResponseEntity<GetProductsResponseDto> getSellerProducts(int page, int size) {
        return ResponseEntity.ok(new GetProductsResponseDto(productService
                .findProducts(SecurityHelper.getUserId(), page, size)
                .stream()
                .map(ProductApiAdapter::adapt)
                .toList()
        ));
    }

    @Override
    public ResponseEntity<Void> createProduct(CreateProductDto createProductDto) {
        UUID productId = productService.createProduct(SecurityHelper.getUserId(), ProductApiAdapter.adapt(createProductDto));
        return ResponseEntity.created(URI.create("/%s".formatted(productId))).build();
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(UUID productId) {
        return ResponseEntity.of(productService.findProductById(productId).map(ProductApiAdapter::adapt));
    }

    @Override
    public void updateProduct(UUID productId, UpdateProductDto productDto) {
        productService.updateProduct(SecurityHelper.getUserId(), productId, ProductApiAdapter.adapt(productDto));
    }

    @Override
    public void deleteProduct(UUID productId) {
        productService.deleteProduct(SecurityHelper.getUserId(), productId);
    }
}
