package com.acme.vendingmachine.application;


import com.acme.common.domain.ServiceException;
import com.acme.product.domain.Product;
import com.acme.product.domain.ProductRepository;
import com.acme.vendingmachine.domain.Coins;
import com.acme.vendingmachine.domain.VendingMachineAccount;
import com.acme.vendingmachine.domain.VendingMachineAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VendingMachineBuyUseCase {
    private final VendingMachineAccountRepository vendingMachineAccountRepository;
    private final ProductRepository productRepository;

    public VendingMachineBuyUseCase(VendingMachineAccountRepository vendingMachineAccountRepository, ProductRepository productRepository) {
        this.vendingMachineAccountRepository = vendingMachineAccountRepository;
        this.productRepository = productRepository;
    }

    public PurchaseResponse buy(UUID buyerId, PurchaseRequest purchaseRequest) {
        VendingMachineAccount vendingMachineAccount = vendingMachineAccountRepository.findByUserId(buyerId)
                .orElseThrow(ServiceException.EntityNotFoundException::new);
        Product product = productRepository.findById(purchaseRequest.productId())
                .orElseThrow(ServiceException.EntityNotFoundException::new);

        int totalCostInCents = product.getTotalCostInCents(purchaseRequest.amount());
        product.decreaseAmountAvailable(purchaseRequest.amount());
        List<Coins> change = vendingMachineAccount.withdraw(totalCostInCents);

        vendingMachineAccountRepository.save(vendingMachineAccount);
        productRepository.save(product);
        return new PurchaseResponse(totalCostInCents, product.getProductName(), change);
    }


    public record PurchaseRequest(UUID productId, int amount) {
    }

    public record PurchaseResponse(int totalSpentInCents, String purchasedProductName, List<Coins> change) {
    }

}
