package com.acme.vendingmachine.domain;

import com.acme.common.domain.ServiceException.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VendingMachineAccountTest {

    @Test
    void shouldUpdateAvailableBalanceAfterDeposit() {
        // given
        VendingMachineAccount account = new VendingMachineAccount(UUID.randomUUID(), UUID.randomUUID(), 50, 1);

        // when
        account.deposit(Coins.TEN);

        // then
        assertEquals(60, account.getBalanceInCents());
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoChangeAvailableAfterAPurchase() {
        // given
        VendingMachineAccount account = new VendingMachineAccount(UUID.randomUUID(), UUID.randomUUID(), 50, 1);

        // when
        List<Coins> change = account.withdraw(50);

        // then
        assertEquals(List.of(), change);
        assertEquals(0, account.getBalanceInCents());
    }

    @Test
    void shouldReturnSomeChangeWhenBalanceIsStillPositiveAfterWithdrawing() {
        // given
        VendingMachineAccount account = new VendingMachineAccount(UUID.randomUUID(), UUID.randomUUID(), 50, 1);

        // when
        List<Coins> change = account.withdraw(35);

        // then
        assertEquals(List.of(Coins.FIVE, Coins.TEN), change);
        assertEquals(0, account.getBalanceInCents());
    }

    @Test
    void shouldReturnSomeChangeButStillHaveAPositiveBalanceWhenChangeCannotBeEntirelyReturnedAsCoins() {
        // given
        VendingMachineAccount account = new VendingMachineAccount(UUID.randomUUID(), UUID.randomUUID(), 50, 1);

        // when
        List<Coins> change = account.withdraw(33);

        // then
        assertEquals(List.of(Coins.FIVE, Coins.TEN), change);
        assertEquals(2, account.getBalanceInCents());
    }

    @Test
    void shouldThrowAnExceptionAndDontChangeTheBalanceWhenTyingToWithdrawMoreThanItsAvailable() {
        // given
        VendingMachineAccount account = new VendingMachineAccount(UUID.randomUUID(), UUID.randomUUID(), 50, 1);

        // when
        Executable illegalOperation = () -> account.withdraw(55);

        // then
        assertThrows(ConflictException.class, illegalOperation);
        assertEquals(50, account.getBalanceInCents());
    }
}