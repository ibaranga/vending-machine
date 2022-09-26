package com.acme.common.infrastructure;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GenericAdapters {
    public static BigDecimal toPriceWithDecimals(int priceInCents) {
        return new BigDecimal(priceInCents).divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY);
    }

    public static int toPriceInCents(BigDecimal priceWithDecimals) {
        return priceWithDecimals.multiply(new BigDecimal(100)).intValueExact();
    }
}
