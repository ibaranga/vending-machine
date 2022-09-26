package com.acme.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/product")
public interface ProductApi {
    @GetMapping()
    ResponseEntity<GetProductsResponseDto> getProducts(
            @RequestParam(name = "page", defaultValue = "0") @Valid @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "10") @Valid @Max(100) int size);

    @GetMapping("/seller")
    ResponseEntity<GetProductsResponseDto> getSellerProducts(
            @RequestParam(name = "page", defaultValue = "0") @Valid @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "10") @Valid @Max(100) int size);

    @PostMapping()
    ResponseEntity<Void> createProduct(@RequestBody @Valid CreateProductDto createProductDto);

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProduct(@PathVariable("productId") UUID productId);

    @PutMapping("/{productId}")
    void updateProduct(@PathVariable("productId") UUID productId, @RequestBody @Valid UpdateProductDto productDto);

    @DeleteMapping("/{productId}")
    void deleteProduct(@PathVariable("productId") UUID productId);

    record CreateProductDto(@NotNull @Size(min = 2, max = 100) String productName,
                            @NotNull @Min(0) Integer amountAvailable,
                            @NotNull @Min(0) BigDecimal cost) {
    }

    record ProductDto(
            @NotNull UUID id,
            @NotNull Integer amountAvailable,
            @NotNull BigDecimal cost,
            @NotNull String productName,
            @NotNull UUID sellerId
    ) {
    }

    record UpdateProductDto(@Size(min = 3) String productName,
                            @Min(0) Integer amountAvailable,
                            @Min(0) BigDecimal cost) {
    }

    record GetProductsResponseDto(@NotNull List<ProductDto> products) {
    }
}
