package com.acme.vendingmachine.application;

import com.acme.common.domain.ServiceException;
import com.acme.testsupport.mocks.MockProductRepository;
import com.acme.testsupport.mocks.MockVendingMachineAccountRepository;
import com.acme.vendingmachine.application.VendingMachineBuyUseCase.PurchaseRequest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class VendingMachineBuyUseCaseTest {

    @Test
    void buy() {
        MockVendingMachineAccountRepository vendingMachineAccountRepository = new MockVendingMachineAccountRepository();
        MockProductRepository productRepository = new MockProductRepository();

        VendingMachineBuyUseCase vendingMachineBuyUseCase = new VendingMachineBuyUseCase(vendingMachineAccountRepository, productRepository);

        assertThrows(ServiceException.EntityNotFoundException.class,
                () -> vendingMachineBuyUseCase.buy(UUID.randomUUID(), new PurchaseRequest(UUID.randomUUID(), 1)));
    }


}