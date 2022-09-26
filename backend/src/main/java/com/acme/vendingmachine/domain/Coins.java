package com.acme.vendingmachine.domain;

public enum Coins {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100);

    private final int cents;

    Coins(int cents) {
        this.cents = cents;
    }

    public int getCents() {
        return cents;
    }
}
