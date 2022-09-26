package com.acme.product.infrastructure;

import com.acme.product.domain.Product;

class ProductEntityAdapter {
    public static Product adapt(ProductEntity productEntity) {
        return new Product(productEntity.getId(),
                productEntity.getAmountAvailable(),
                productEntity.getCostInCents(),
                productEntity.getProductName(),
                productEntity.getSellerId(),
                productEntity.getVersion()
        );
    }

    public static ProductEntity adapt(Product product) {
        return new ProductEntity(product.getId(),
                product.getAmountAvailable(),
                product.getCostInCents(),
                product.getProductName(),
                product.getSellerId(),
                product.getVersion(),
                null,
                null
        );
    }

}
