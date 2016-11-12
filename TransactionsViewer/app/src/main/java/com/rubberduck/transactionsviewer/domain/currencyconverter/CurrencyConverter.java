package com.rubberduck.transactionsviewer.domain.currencyconverter;

import com.rubberduck.transactionsviewer.domain.exception.CurrencyConversionFailedException;
import com.rubberduck.transactionsviewer.domain.model.Amount;

/**
 * An abstraction for a currency converter able to convert a given {@link Amount} in one currency to another.
 */
public interface CurrencyConverter {

    /**
     * Converts a given {@link Amount} to an {@link Amount} in another currency.
     * @param source The {@link Amount} to be converted
     * @param toCurrency The currency to which the amount has to be converted
     * @return {@link Amount} in #toCurrency
     * @throws CurrencyConversionFailedException If the source {@link Amount} cannot be converted
     */
    Amount convert(Amount source, String toCurrency) throws CurrencyConversionFailedException;
}
