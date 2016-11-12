package com.rubberduck.transactionsviewer.domain.model;

public class Transaction {

    private final Product product;
    private final Amount amount;

    public Transaction(Product product, Amount amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public Amount getAmount() {
        return amount;
    }
}
