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
}
