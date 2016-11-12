package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.currencyconverter.CurrencyConverter;
import com.rubberduck.transactionsviewer.domain.exception.CurrencyConversionFailedException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.model.Amount;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A use case to convert a given list of amounts to another currency.
 * If a given amount cannot be converted, null is returned.
 * Call {@link #init(List, String)} before executing the use case.
 */
public class ConvertAmountsUseCase extends UseCase<List<Amount>> {

    private final CurrencyConverter currencyConverter;

    private List<Amount> fromAmounts;
    private String toCurrency;

    @Inject
    ConvertAmountsUseCase(ThreadExecutor threadExecutor,
                          MainThread mainThread,
                          CurrencyConverter currencyConverter) {
        super(threadExecutor, mainThread);
        this.currencyConverter = currencyConverter;
    }

    public ConvertAmountsUseCase init(List<Amount> fromAmounts, String toCurrency) {
        if (fromAmounts == null || fromAmounts.size() == 0) {
            throw new IllegalArgumentException("From amounts list cannot be null or empty.");
        }
        if (toCurrency == null || toCurrency.length() == 0) {
            throw new IllegalArgumentException("Target currency cannot be null or blank.");
        }

        this.fromAmounts = fromAmounts;
        this.toCurrency = toCurrency;

        return this;
    }

    @Override
    void performUseCase(final UseCaseCallback<List<Amount>> callback) {
        if (fromAmounts == null || toCurrency == null) {
            throw new IllegalArgumentException("Call init(fromAmounts, toCurrency) before executing the use case.");
        }

        final List<Amount> resultAmounts = new ArrayList<>(fromAmounts.size());
        for (Amount fromAmount : fromAmounts) {
            Amount result;
            try {
                result = currencyConverter.convert(fromAmount, toCurrency);
            } catch (CurrencyConversionFailedException e) {
                result = null;
            }

            resultAmounts.add(result);
        }

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resultAmounts);
            }
        });
    }
}
