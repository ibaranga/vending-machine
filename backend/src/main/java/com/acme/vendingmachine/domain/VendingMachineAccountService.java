package com.acme.vendingmachine.domain;

import com.acme.common.domain.ServiceException;

import java.util.UUID;

public class VendingMachineAccountService {
    private final VendingMachineAccountRepository vendingMachineAccountRepository;

    public VendingMachineAccountService(VendingMachineAccountRepository vendingMachineAccountRepository) {
        this.vendingMachineAccountRepository = vendingMachineAccountRepository;
    }

    public VendingMachineAccount findByBuyerId(UUID buyerId) {
        return this.vendingMachineAccountRepository.findByUserId(buyerId)
                .orElseThrow(ServiceException.EntityNotFoundException::new);
    }

    public void deposit(UUID buyerId, Coins coins) {
        VendingMachineAccount vendingMachineAccount = vendingMachineAccountRepository.findByUserId(buyerId)
                .orElseGet(() -> VendingMachineAccount.create(buyerId));
        vendingMachineAccount.deposit(coins);
        vendingMachineAccountRepository.save(vendingMachineAccount);
    }

    public void reset(UUID buyerId) {
        VendingMachineAccount vendingMachineAccount = vendingMachineAccountRepository.findByUserId(buyerId)
                .orElseThrow(ServiceException.EntityNotFoundException::new);
        vendingMachineAccount.reset();
        vendingMachineAccountRepository.save(vendingMachineAccount);
    }
}
