package com.rubberduck.transactionsviewer.domain.model;

public class Product {

    private final String sku;

    public Product(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

}