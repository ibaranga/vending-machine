package com.acme.vendingmachine.domain;

import com.acme.common.domain.ServiceException.ConflictException;
import lombok.Getter;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
public class VendingMachineAccount {
    private static final List<Coins> COINS_DESCENDING_VALUE_ORDER = Stream.of(Coins.values())
            .sorted(Comparator.comparingInt(Coins::getCents).reversed())
            .toList();
    private final UUID id;
    private final UUID buyerId;
    private final long version;

    private int balanceInCents;

    public VendingMachineAccount(UUID id, UUID buyerId, int balanceInCents, long version) {
        this.id = id;
        this.buyerId = buyerId;
        this.balanceInCents = balanceInCents;
        this.version = version;
    }

    public static VendingMachineAccount create(UUID buyerId) {
        return new VendingMachineAccount(UUID.randomUUID(), buyerId, 0, 1);
    }

    /**
     * Deposit into account
     *
     * @param coins
     */
    public void deposit(Coins coins) {
        balanceInCents += coins.getCents();
    }

    /**
     * Withdraw from account and return the change, if any
     *
     * @param amountInCents the amount, in cents, to withdraw
     * @return change, as a list of coins representing the balance left after withdrawing `amountInCents` from balance
     */
    public List<Coins> withdraw(int amountInCents) {
        if (balanceInCents < amountInCents) {
            throw new ConflictException();
        }
        balanceInCents -= amountInCents;

        EnumMap<Coins, Integer> result = new EnumMap<>(Coins.class);
        for (Coins coins : COINS_DESCENDING_VALUE_ORDER) {
            int coinsAmount = balanceInCents / coins.getCents();
            if (coinsAmount > 0) {
                balanceInCents -= coinsAmount * coins.getCents();
                result.put(coins, coinsAmount);
            }
        }
        return result.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey().getCents()))
                .flatMap(e -> IntStream.range(0, e.getValue()).mapToObj(index -> e.getKey()))
                .toList();
    }

    /**
     * Reset the balance to 0
     */
    public void reset() {
        this.balanceInCents = 0;
    }
}
