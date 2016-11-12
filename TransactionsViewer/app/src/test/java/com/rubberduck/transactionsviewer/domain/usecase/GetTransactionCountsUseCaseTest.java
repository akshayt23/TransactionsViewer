package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class GetTransactionCountsUseCaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private MainThread mainThread;
    @Mock
    ProductTransactionsRepository repository;
    @Mock
    UseCaseCallback<List<Integer>> callback;

    @Captor
    private ArgumentCaptor<Runnable> mainThreadRunnableCaptor;

    private List<String> dummyProductSkus;
    private List<Integer> dummyTransactionCounts;

    private GetTransactionCountsUseCase getTransactionCountsUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getTransactionCountsUseCase = new GetTransactionCountsUseCase(threadExecutor, mainThread,
                                                                      repository);
    }

    @Test
    public void shouldSendResultToCallbackOnMainThreadOnSuccess() throws ProductNotFoundException {
        dummyProductSkus = Arrays.asList("Sku1", "Sku2", "Sku3", "Sku4");
        dummyTransactionCounts = Arrays.asList(23, 7, 37, 17);

        for (int i = 0; i < 4; i++) {
            when(repository.getTransactionsCount(dummyProductSkus.get(i)))
                    .thenReturn(dummyTransactionCounts.get(i));
        }

        getTransactionCountsUseCase.init(dummyProductSkus).performUseCase(callback);

        verify(repository, times(4)).getTransactionsCount(anyString());
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onSuccess(dummyTransactionCounts);
    }

    @Test
    public void returnTransactionCountAsZeroWhenProductNotFound() throws ProductNotFoundException {
        dummyProductSkus = Arrays.asList("Sku1", "Sku2", "Sku3", "Sku4");
        dummyTransactionCounts = Arrays.asList(23, 0, 7, 17);

        when(repository.getTransactionsCount(dummyProductSkus.get(0))).thenReturn(dummyTransactionCounts.get(0));
        when(repository.getTransactionsCount(dummyProductSkus.get(1))).thenThrow(
                new ProductNotFoundException("Can't find product!"));
        when(repository.getTransactionsCount(dummyProductSkus.get(2))).thenReturn(dummyTransactionCounts.get(2));
        when(repository.getTransactionsCount(dummyProductSkus.get(3))).thenReturn(dummyTransactionCounts.get(3));

        getTransactionCountsUseCase.init(dummyProductSkus).performUseCase(callback);

        verify(repository, times(4)).getTransactionsCount(anyString());
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onSuccess(dummyTransactionCounts);
    }
}