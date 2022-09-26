package com.acme.product.api;

import com.acme.common.infrastructure.GenericAdapters;
import com.acme.product.api.ProductApi.ProductDto;
import com.acme.product.domain.Product;
import com.acme.product.domain.Product.CreateProductPayload;
import com.acme.product.domain.Product.UpdateProductPayload;

public class ProductApiAdapter {

    public static ProductDto adapt(Product product) {
        return new ProductDto(product.getId(),
                product.getAmountAvailable(),
                GenericAdapters.toPriceWithDecimals(product.getCostInCents()),
                product.getProductName(),
                product.getSellerId());
    }

    public static CreateProductPayload adapt(ProductApi.CreateProductDto createProductDto) {
        return new CreateProductPayload(
                createProductDto.amountAvailable(),
                GenericAdapters.toPriceInCents(createProductDto.cost()),
                createProductDto.productName()
        );
    }

    public static UpdateProductPayload adapt(ProductApi.UpdateProductDto updateProductDto) {
        return new UpdateProductPayload(
                updateProductDto.amountAvailable(),
                updateProductDto.cost() == null ? null : GenericAdapters.toPriceInCents(updateProductDto.cost()),
                updateProductDto.productName()
        );

    }

}
