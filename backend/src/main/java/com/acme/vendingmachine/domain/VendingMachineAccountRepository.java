package com.acme.vendingmachine.domain;

import java.util.Optional;
import java.util.UUID;

public interface VendingMachineAccountRepository {
    Optional<VendingMachineAccount> findByUserId(UUID id);
    UUID save(VendingMachineAccount vendingMachineAccount);
}
