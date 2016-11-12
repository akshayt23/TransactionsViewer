package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.currencyconverter.CurrencyConverter;
import com.rubberduck.transactionsviewer.domain.exception.CurrencyConversionFailedException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.model.Amount;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ConvertAmountsUseCaseTest {

    private final String toCurrency = "GBP";

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private MainThread mainThread;
    @Mock
    CurrencyConverter currencyConverter;
    @Mock
    UseCaseCallback<List<Amount>> callback;

    @Captor
    private ArgumentCaptor<Runnable> mainThreadRunnableCaptor;

    private List<Amount> dummyAmounts;
    private List<Amount> dummyAmountsInGbp;

    private ConvertAmountsUseCase convertAmountsUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        convertAmountsUseCase = new ConvertAmountsUseCase(threadExecutor, mainThread, currencyConverter);
    }

    @Test
    public void shouldSendResultToCallbackOnMainThreadAfterFetchingProducts() throws CurrencyConversionFailedException {
        dummyAmounts = Arrays.asList(new Amount("USD", 30.6), new Amount("GBP", 23.7), new Amount("CAD", 29.3));
        dummyAmountsInGbp = Arrays.asList(new Amount(toCurrency, 23.5687),
                                          new Amount(toCurrency, 23.7),
                                          new Amount(toCurrency, 20.762));

        for (int i = 0; i < 3; i++) {
            when(currencyConverter.convert(dummyAmounts.get(i), toCurrency))
                    .thenReturn(dummyAmountsInGbp.get(i));
        }

        convertAmountsUseCase.init(dummyAmounts, toCurrency)
                             .performUseCase(callback);

        verify(currencyConverter, times(3)).convert(any(Amount.class), anyString());
        verifyNoMoreInteractions(currencyConverter);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onSuccess(dummyAmountsInGbp);
    }

    @Test
    public void returnResultantAmountAsNullWhenCurrencyConversionFails() throws CurrencyConversionFailedException {
        dummyAmounts = Arrays.asList(new Amount("USD", 30.6), new Amount("GBP", 23.7), new Amount("CAD", 29.3));
        dummyAmountsInGbp = Arrays.asList(new Amount(toCurrency, 23.5687),
                                          new Amount(toCurrency, 23.7),
                                          null);

        when(currencyConverter.convert(dummyAmounts.get(0), toCurrency)).thenReturn(dummyAmountsInGbp.get(0));
        when(currencyConverter.convert(dummyAmounts.get(1), toCurrency)).thenReturn(dummyAmountsInGbp.get(1));
        when(currencyConverter.convert(dummyAmounts.get(2), toCurrency)).thenThrow(
                new CurrencyConversionFailedException("Failed to convert currency."));

        convertAmountsUseCase.init(dummyAmounts, toCurrency)
                             .performUseCase(callback);

        verify(currencyConverter, times(3)).convert(any(Amount.class), anyString());
        verifyNoMoreInteractions(currencyConverter);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onSuccess(dummyAmountsInGbp);
    }
}