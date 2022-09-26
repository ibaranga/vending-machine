package com.acme.vendingmachine.domain;

import com.acme.common.domain.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class VendingMachineAccountServiceTest {

    @Test
    void deposit() {
        MockVendingMachineAccountRepository vendingMachineAccountRepository = new MockVendingMachineAccountRepository();
        VendingMachineAccountService vendingMachineAccountService = new VendingMachineAccountService(vendingMachineAccountRepository);

        vendingMachineAccountService.deposit(UUID.randomUUID(), Coins.TEN);
    }

    @Test
    void reset() {
        MockVendingMachineAccountRepository vendingMachineAccountRepository = new MockVendingMachineAccountRepository();
        VendingMachineAccountService vendingMachineAccountService = new VendingMachineAccountService(vendingMachineAccountRepository);
        assertThrows(ServiceException.EntityNotFoundException.class, () -> vendingMachineAccountService.reset(UUID.randomUUID()));
    }

    static class MockVendingMachineAccountRepository implements VendingMachineAccountRepository {
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

}