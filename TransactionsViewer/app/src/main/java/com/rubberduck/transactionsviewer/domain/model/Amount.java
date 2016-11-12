package com.rubberduck.transactionsviewer.domain.model;

public class Amount {

    private final String currency;
    private final double value;

    public Amount(String currency, double value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public double getValue() {
        return value;
    }

}
