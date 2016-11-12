package com.rubberduck.transactionsviewer.data.currencyconverter;

import android.content.Context;

import com.rubberduck.transactionsviewer.data.serializer.JsonSerializer;
import com.rubberduck.transactionsviewer.domain.currencyconverter.CurrencyConverter;
import com.rubberduck.transactionsviewer.domain.exception.CurrencyConversionFailedException;
import com.rubberduck.transactionsviewer.domain.model.Amount;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * An implementation of a {@link CurrencyConverter} that gets the conversion rates from the local filesystem.
 */
@Singleton
public class LocalCurrencyConverter implements CurrencyConverter {

    private static final String CONVERSION_RATES_JSON_FILE = "rates.json";

    private final Context context;
    private final JsonSerializer jsonSerializer;

    @Inject
    LocalCurrencyConverter(Context context, JsonSerializer jsonSerializer) {
        this.context = context;
        this.jsonSerializer = jsonSerializer;
    }

    @Override
    public Amount convert(Amount source, String toCurrency) throws CurrencyConversionFailedException {
        throw new CurrencyConversionFailedException("Failed to read conversion rates from file.");
    }
}
