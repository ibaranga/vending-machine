package com.acme.vendingmachine.api;

import com.acme.common.api.SecurityHelper;
import com.acme.common.infrastructure.GenericAdapters;
import com.acme.vendingmachine.application.VendingMachineBuyUseCase;
import com.acme.vendingmachine.application.VendingMachineBuyUseCase.PurchaseResponse;
import com.acme.vendingmachine.domain.VendingMachineAccount;
import com.acme.vendingmachine.domain.VendingMachineAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendingmachine")
public class VendingMachineController implements VendingMachineApi {
    private final VendingMachineBuyUseCase vendingMachineBuyUseCase;
    private final VendingMachineAccountService vendingMachineAccountService;

    public VendingMachineController(VendingMachineBuyUseCase vendingMachineBuyUseCase, VendingMachineAccountService vendingMachineAccountService) {
        this.vendingMachineBuyUseCase = vendingMachineBuyUseCase;
        this.vendingMachineAccountService = vendingMachineAccountService;
    }

    @Override
    public ResponseEntity<GetBalanceResponseDto> getBalance() {
        VendingMachineAccount account = this.vendingMachineAccountService.findByBuyerId(SecurityHelper.getUserId());
        return ResponseEntity.ok(new GetBalanceResponseDto(GenericAdapters.toPriceWithDecimals(account.getBalanceInCents())));
    }

    @Override
    public ResponseEntity<Void> deposit(DepositRequestDto depositRequest) {
        vendingMachineAccountService.deposit(SecurityHelper.getUserId(), VendingMachineApiAdapter.adapt(depositRequest.coins()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> reset() {
        vendingMachineAccountService.reset(SecurityHelper.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PurchaseResponseDto> buy(PurchaseRequestDto purchaseRequest) {
        PurchaseResponse purchaseResponse = vendingMachineBuyUseCase.buy(SecurityHelper.getUserId(), VendingMachineApiAdapter.adapt(purchaseRequest));
        return ResponseEntity.ok(VendingMachineApiAdapter.adapt(purchaseResponse));
    }
}
