package com.acme.testsupport.mocks;

import com.acme.vendingmachine.domain.VendingMachineAccount;
import com.acme.vendingmachine.domain.VendingMachineAccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MockVendingMachineAccountRepository implements VendingMachineAccountRepository {
    private final Map<UUID, VendingMachineAccount> accountsByBuyerId = new HashMap<>();

    @Override
    public Optional<VendingMachineAccount> findByUserId(UUID id) {
        return Optional.ofNullable(accountsByBuyerId.get(id));
    }

    @Override
    public UUID save(VendingMachineAccount vendingMachineAccount) {
        accountsByBuyerId.put(vendingMachineAccount.getBuyerId(), vendingMachineAccount);
        return vendingMachineAccount.getId();
    }
}
