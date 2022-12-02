package com.acme.vendingmachine.api;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("vendingmachine")
public interface VendingMachineApi {
    @GetMapping("/balance")
    ResponseEntity<GetBalanceResponseDto> getBalance();

    @PostMapping("/deposit")
    ResponseEntity<Void> deposit(@RequestBody @Valid DepositRequestDto depositRequest);

    @PostMapping("/buy")
    ResponseEntity<PurchaseResponseDto> buy(@RequestBody @Valid PurchaseRequestDto purchaseRequest);

    @PostMapping("/reset")
    ResponseEntity<Void> reset();

    enum ApiCoins {
        FIVE(5),
        TEN(10),
        TWENTY(20),
        FIFTY(50),
        HUNDRED(100);

        @JsonValue
        private final int cents;

        ApiCoins(int cents) {
            this.cents = cents;
        }

        public int getCents() {
            return cents;
        }
    }

    record DepositRequestDto(@NotNull ApiCoins coins) {
    }

    record PurchaseRequestDto(@NotNull UUID productId, @NotNull @Min(1) Integer amount) {
    }

    record PurchaseResponseDto(BigDecimal totalSpentAmount, String purchasedProductName, ApiCoins[] change) {
    }

    record GetBalanceResponseDto(BigDecimal balance) {
    }
}
