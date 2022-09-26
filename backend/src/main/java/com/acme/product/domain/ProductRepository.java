package com.acme.product.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findById(UUID id);

    UUID save(Product product);

    List<Product> findAll(int page, int size);

    List<Product> findAllBySellerId(UUID sellerId, int page, int size);

    void delete(Product product);
}
