package com.acme.product.domain;

import com.acme.common.domain.ServiceException.EntityNotFoundException;
import com.acme.product.domain.Product.CreateProductPayload;
import com.acme.product.domain.Product.UpdateProductPayload;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public UUID createProduct(UUID sellerId, CreateProductPayload createProductPayload) {
        Product product = Product.create(sellerId, createProductPayload);
        return productRepository.save(product);
    }

    public void updateProduct(UUID sellerId, UUID productId, UpdateProductPayload updateProductPayload) {
        Product product = findProductByIdOwnedBySeller(sellerId, productId).update(updateProductPayload);
        productRepository.save(product);
    }

    public void deleteProduct(UUID sellerId, UUID productId) {
        Product product = findProductByIdOwnedBySeller(sellerId, productId);
        productRepository.delete(product);
    }

    public List<Product> findProducts(int page, int size) {
        return productRepository.findAll(page, size);
    }

    public List<Product> findProducts(UUID sellerId, int page, int size) {
        return productRepository.findAllBySellerId(sellerId, page, size);
    }

    public Optional<Product> findProductById(UUID id) {
        return productRepository.findById(id);
    }


    protected Product findProductByIdOwnedBySeller(UUID sellerId, UUID productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(EntityNotFoundException::new)
                .assertOwnership(sellerId);
    }
}
