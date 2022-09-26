package com.acme.testsupport.mocks;

import com.acme.product.domain.Product;
import com.acme.product.domain.ProductRepository;

import java.util.*;

public class MockProductRepository implements ProductRepository {
    private final Map<UUID, Product> productsById = new HashMap<>();

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(productsById.get(id));
    }

    @Override
    public UUID save(Product product) {
        productsById.put(product.getId(), product);
        return product.getId();
    }

    @Override
    public List<Product> findAll(int page, int size) {
        return List.copyOf(productsById.values());
    }

    @Override
    public List<Product> findAllBySellerId(UUID sellerId, int page, int size) {
        return productsById.values().stream().filter(product -> product.getSellerId().equals(sellerId)).toList();
    }

    @Override
    public void delete(Product product) {
        productsById.remove(product.getId());
    }
}
