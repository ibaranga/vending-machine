package com.acme.testsupport.apiclient;

import com.acme.vendingmachine.api.VendingMachineApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class VendingMachineApiClient implements VendingMachineApi {
    private final RestTemplate restTemplate;

    public VendingMachineApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<GetBalanceResponseDto> getBalance() {
        return restTemplate.getForEntity("/vendingmachine/balance", GetBalanceResponseDto.class);
    }

    @Override
    public ResponseEntity<Void> deposit(DepositRequestDto depositRequest) {
        return restTemplate.postForEntity("/vendingmachine/deposit", depositRequest, Void.class);
    }

    @Override
    public ResponseEntity<PurchaseResponseDto> buy(PurchaseRequestDto purchaseRequest) {
        return restTemplate.postForEntity("/vendingmachine/buy", purchaseRequest, PurchaseResponseDto.class);
    }

    @Override
    public ResponseEntity<Void> reset() {
        RequestEntity<Void> entity = RequestEntity
                .post("/vendingmachine/reset")
                .header(HttpHeaders.CONTENT_TYPE, "application/json").build();
        return restTemplate.exchange(entity, Void.class);
    }
}
