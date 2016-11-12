package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.exception.NoProductsAvailableException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.model.Product;
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

public class GetAllProductsUseCaseTest {

    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private MainThread mainThread;
    @Mock
    ProductTransactionsRepository repository;
    @Mock
    UseCaseCallback<List<Product>> callback;

    @Captor
    private ArgumentCaptor<Runnable> mainThreadRunnableCaptor;

    private GetAllProductsUseCase getAllProductsUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getAllProductsUseCase = new GetAllProductsUseCase(threadExecutor, mainThread, repository);
    }

    @Test
    public void shouldSendResultToCallbackOnMainThreadAfterFetchingProducts() throws NoProductsAvailableException {
        List<Product> dummyProducts = Arrays.asList(new Product("Sku1"), new Product("Sku2"),
                                                    new Product("Sku3"), new Product("Sku4"));
        when(repository.getAllProducts()).thenReturn(dummyProducts);

        getAllProductsUseCase.performUseCase(callback);

        verify(repository).getAllProducts();
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onSuccess(dummyProducts);
    }

    @Test
    public void shouldSendErrorBundleToCallbackOnMainThreadOnFailure() throws NoProductsAvailableException {
        when(repository.getAllProducts()).thenThrow(new NoProductsAvailableException("No products available"));

        getAllProductsUseCase.performUseCase(callback);

        verify(repository).getAllProducts();
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(threadExecutor);

        verify(mainThread, times(1)).post(mainThreadRunnableCaptor.capture());
        mainThreadRunnableCaptor.getValue().run();
        verify(callback).onFailure(any(ErrorBundle.class));
    }
}