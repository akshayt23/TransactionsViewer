package com.rubberduck.transactionsviewer.presentation.model;

public class TransactionViewModel {

    private final String originalAmount;
    private final String convertedAmount;

    public TransactionViewModel(String originalAmount, String convertedAmount) {
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof TransactionViewModel)) {
            return false;
        }

        TransactionViewModel transactionViewModel = (TransactionViewModel) obj;
        return (this.originalAmount.equals(transactionViewModel.getOriginalAmount()) &&
                this.convertedAmount.equals(transactionViewModel.getConvertedAmount()));
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.originalAmount.hashCode();
        hash = 23 * hash + this.convertedAmount.hashCode();
        return hash;
    }
}
