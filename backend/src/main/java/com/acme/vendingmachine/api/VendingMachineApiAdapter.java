package com.acme.vendingmachine.api;

import com.acme.common.domain.ServiceException;
import com.acme.common.infrastructure.GenericAdapters;
import com.acme.vendingmachine.api.VendingMachineApi.ApiCoins;
import com.acme.vendingmachine.api.VendingMachineApi.PurchaseRequestDto;
import com.acme.vendingmachine.api.VendingMachineApi.PurchaseResponseDto;
import com.acme.vendingmachine.application.VendingMachineBuyUseCase.PurchaseRequest;
import com.acme.vendingmachine.application.VendingMachineBuyUseCase.PurchaseResponse;
import com.acme.vendingmachine.domain.Coins;

import java.util.stream.Stream;

public class VendingMachineApiAdapter {
    public static PurchaseRequest adapt(PurchaseRequestDto purchaseRequestDto) {
        return new PurchaseRequest(purchaseRequestDto.productId(), purchaseRequestDto.amount());
    }

    public static PurchaseResponseDto adapt(PurchaseResponse purchaseResponse) {
        return new PurchaseResponseDto(
                GenericAdapters.toPriceWithDecimals(purchaseResponse.totalSpentInCents()),
                purchaseResponse.purchasedProductName(),
                purchaseResponse.change().stream().map(VendingMachineApiAdapter::adapt).sorted().toArray(ApiCoins[]::new)
        );
    }

    public static ApiCoins adapt(Coins coins) {
        return ApiCoins.valueOf(coins.toString());
    }

    public static Coins adapt(ApiCoins apiCoin) {
        return Stream.of(Coins.values()).filter(coin -> coin.getCents() == apiCoin.getCents()).findFirst()
                .orElseThrow(ServiceException.InvalidRequest::new);
    }
}
