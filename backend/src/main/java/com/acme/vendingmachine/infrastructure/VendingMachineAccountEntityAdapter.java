package com.acme.vendingmachine.infrastructure;

import com.acme.vendingmachine.domain.VendingMachineAccount;

public class VendingMachineAccountEntityAdapter {
    public static VendingMachineAccount adapt(VendingMachineAccountEntity vendingMachineAccountEntity) {
        return new VendingMachineAccount(vendingMachineAccountEntity.getId(),
                vendingMachineAccountEntity.getUserId(),
                vendingMachineAccountEntity.getBalance(),
                vendingMachineAccountEntity.getVersion());
    }

    public static VendingMachineAccountEntity adapt(VendingMachineAccount vendingMachineAccount) {
        return new VendingMachineAccountEntity(vendingMachineAccount.getId(),
                vendingMachineAccount.getBuyerId(),
                vendingMachineAccount.getBalanceInCents(),
                vendingMachineAccount.getVersion(),
                null,
                null);
    }
}
