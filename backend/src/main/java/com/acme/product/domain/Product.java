package com.acme.product.domain;

import com.acme.common.domain.ServiceException;
import com.acme.common.domain.ServiceException.PermissionDeniedException;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Product {
    private final UUID id;
    private final UUID sellerId;
    private final long version;
    private int amountAvailable;
    private int costInCents;
    private String productName;

    public Product(UUID id, Integer amountAvailable, int costInCents, String productName, UUID sellerId, long version) {
        this.id = id;
        this.amountAvailable = amountAvailable;
        this.costInCents = costInCents;
        this.productName = productName;
        this.sellerId = sellerId;
        this.version = version;
    }

    public static Product create(UUID sellerId, CreateProductPayload createProductPayload) {
        return new Product(UUID.randomUUID(),
                createProductPayload.amountAvailable,
                createProductPayload.costInCents,
                createProductPayload.productName,
                sellerId,
                1);
    }

    public Product decreaseAmountAvailable(Integer amount) {
        if (amountAvailable - amount < 0) {
            throw new ServiceException.ConflictException();
        }
        this.amountAvailable -= amount;
        return this;
    }

    public Product update(UpdateProductPayload updateProductPayload) {
        if (updateProductPayload.productName != null) {
            productName = updateProductPayload.productName;
        }
        if (updateProductPayload.costInCents != null) {
            costInCents = updateProductPayload.costInCents;
        }
        if (updateProductPayload.amountAvailable != null) {
            amountAvailable = updateProductPayload.amountAvailable;
        }
        return this;
    }

    public int getTotalCostInCents(int amount) {
        return costInCents * amount;
    }

    public Product assertOwnership(UUID sellerId) {
        if (!Objects.equals(sellerId, this.sellerId)) {
            throw new PermissionDeniedException("Seller %s doesn't own product %s".formatted(sellerId, this.id));
        }
        return this;
    }

    public record CreateProductPayload(Integer amountAvailable, Integer costInCents, String productName) {
    }

    public record UpdateProductPayload(Integer amountAvailable, Integer costInCents, String productName) {
    }
}
