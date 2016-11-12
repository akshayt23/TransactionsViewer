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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof ProductViewModel)) {
            return false;
        }

        ProductViewModel productViewModel = (ProductViewModel) obj;
        return (this.sku.equals(productViewModel.getSku()) &&
                this.transactionsCount.equals(productViewModel.getTransactionsCount()));
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.sku.hashCode();
        hash = 23 * hash + this.transactionsCount.hashCode();
        return hash;
    }
}
