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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Amount)) {
            return false;
        }

        Amount amount = (Amount) obj;
        return (this.currency.equals(amount.getCurrency()) &&
                this.value == amount.getValue());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.currency.hashCode();
        hash = 23 * hash + Double.valueOf(this.value).hashCode();
        return hash;
    }
}
