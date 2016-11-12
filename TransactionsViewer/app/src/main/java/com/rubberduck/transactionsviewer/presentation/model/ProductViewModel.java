package com.rubberduck.transactionsviewer.presentation.model;

public class ProductViewModel {

    private final String sku;
    private final String transactionsCount;

    public ProductViewModel(String sku, String transactionsCount) {
        this.sku = sku;
        this.transactionsCount = transactionsCount;
    }

    public String getSku() {
        return sku;
    }

    public String getTransactionsCount() {
        return transactionsCount;
    }
}
