package com.rubberduck.transactionsviewer.domain.model;

public class Product {

    private final String sku;

    public Product(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Product)) {
            return false;
        }

        Product product = (Product) obj;
        return (this.sku.equals(product.getSku()));
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.sku.hashCode();
        return hash;
    }
}