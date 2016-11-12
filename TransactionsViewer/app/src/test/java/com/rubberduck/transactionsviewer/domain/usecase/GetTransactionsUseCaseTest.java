package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.model.Amount;
import com.rubberduck.transactionsviewer.domain.model.Product;
import com.rubberduck.transactionsviewer.domain.model.Transaction;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class GetTransactionsUseCaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private MainThread mainThread;
    @Mock
    ProductTransactionsRepository repository;
    @Mock
    UseCaseCallback<List<Transaction>> callback;

    @Captor
    private ArgumentCaptor<Runnable> mainThreadRunnableCaptor;

    private Product dummyProduct;
    private List<Amount> dummyAmounts;
    private List<Transaction> dummyTransactions;

    private GetTransactionsUseCase getTransactionsUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dummyProduct = new Product("dummySku");
        dummyAmounts = Arrays.asList(new Amount("USD", 30.6), new Amount("GBP", 23.7), new Amount("CAD", 29.3));
        dummyTransactions = Arrays.asList(new Transaction(dummyProduct, dummyAmounts.get(0)),
                                          new Transaction(dummyProduct, dummyAmounts.get(1)),
                                          new Transaction(dummyProduct, dummyAmounts.get(2)));

        getTransactionsUseCase = new GetTransactionsUseCase(threadExecutor, mainThread,
                                                            repository, dummyProduct.getSku());
    }

    @Test
    public void shouldSendResultToCallbackOnMainThreadOnSuccess() throws ProductNotFoundException {
        when(repository.getAllTransactions(dummyProduct.getSku())).thenReturn(dummyTransactions);

        getTransactionsUseCase.performUseCase(callback);

        verify(repository).getAllTransactions(dummyProduct.getSku());
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onSuccess(dummyTransactions);
    }

    @Test
    public void shouldSendErrorBundleToCallbackOnMainThreadOnFailure() throws ProductNotFoundException {
        ProductNotFoundException exception = new ProductNotFoundException("Product not found!");
        when(repository.getAllTransactions(dummyProduct.getSku())).thenThrow(exception);

        getTransactionsUseCase.performUseCase(callback);

        verify(repository).getAllTransactions(dummyProduct.getSku());
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onFailure(any(ErrorBundle.class));
    }

}