package com.rubberduck.transactionsviewer.presentation.presenter;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.exception.NoProductsAvailableException;
import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.model.Product;
import com.rubberduck.transactionsviewer.domain.usecase.GetAllProductsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.GetTransactionCountsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.UseCaseCallback;
import com.rubberduck.transactionsviewer.presentation.model.ProductViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductsPresenterTest {

    @Mock
    private ProductsPresenter.View productsView;
    @Mock
    private GetAllProductsUseCase getAllProductsUseCase;
    @Mock
    private GetTransactionCountsUseCase getTransactionCountsUseCase;

    @Captor
    private ArgumentCaptor<UseCaseCallback<List<Product>>> getAllProductsCallbackCaptor;
    @Captor
    private ArgumentCaptor<UseCaseCallback<List<Integer>>> getTransactionCountsCallbackCaptor;

    private List<Product> dummyProducts;
    private List<String> dummyProductSkus;
    private List<Integer> dummyTransactionCounts;
    private List<ProductViewModel> dummyProductViewModels;

    private ProductsPresenter productsPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        productsPresenter = new ProductsPresenter(getAllProductsUseCase, getTransactionCountsUseCase);

        dummyProducts = Arrays.asList(new Product("Sku1"), new Product("Sku2"),
                                      new Product("Sku3"), new Product("Sku4"));

        dummyProductSkus = Arrays.asList("Sku1", "Sku2", "Sku3", "Sku4");
        dummyTransactionCounts = Arrays.asList(14, 7, 23, 37);

        dummyProductViewModels = Arrays.asList(new ProductViewModel("Sku1", "14 transactions"),
                                               new ProductViewModel("Sku2", "7 transactions"),
                                               new ProductViewModel("Sku3", "23 transactions"),
                                               new ProductViewModel("Sku4", "37 transactions"));

        when(getTransactionCountsUseCase.init(dummyProductSkus)).thenReturn(getTransactionCountsUseCase);
    }

    @Test
    public void showProgressBarAndExecuteGetAllProductsUseCaseOnViewAttach() {
        productsPresenter.attachView(productsView);

        verify(productsView).showProgressBar();
        verify(getAllProductsUseCase).execute(ArgumentMatchers.<UseCaseCallback<List<Product>>>any());
    }

    @Test
    public void hideProgressBarAndShowErrorIfProductsRetrievalFails() {
        productsPresenter.attachView(productsView);

        verify(getAllProductsUseCase).execute(getAllProductsCallbackCaptor.capture());
        getAllProductsCallbackCaptor.getValue().onFailure(getNoProductsAvailableErrorBundle());

        verify(productsView).hideProgressBar();
        verify(productsView).showError("No product available!");
    }

    @Test
    public void hideProgressBarAndShowProductsAfterProductsAndTransactionCountsAreRetrieved() {
        verifyTransactionCountsUseCaseExecutedOnProductsRetrieval();
        getTransactionCountsCallbackCaptor.getValue().onSuccess(dummyTransactionCounts);

        verify(productsView).hideProgressBar();
        verify(productsView).showProducts(dummyProductViewModels);
    }

    @Test
    public void hideProgressBarAndShowErrorIfTransactionCountsRetrievalFails() {
        verifyTransactionCountsUseCaseExecutedOnProductsRetrieval();
        getTransactionCountsCallbackCaptor.getValue().onFailure(getProductNotFoundErrorBundle());

        verify(productsView).hideProgressBar();
        verify(productsView).showError("Product not found!");
    }

    private void verifyTransactionCountsUseCaseExecutedOnProductsRetrieval() {
        productsPresenter.attachView(productsView);

        verify(getAllProductsUseCase).execute(getAllProductsCallbackCaptor.capture());
        getAllProductsCallbackCaptor.getValue().onSuccess(dummyProducts);

        verify(getTransactionCountsUseCase).init(dummyProductSkus);
        verify(getTransactionCountsUseCase).execute(getTransactionCountsCallbackCaptor.capture());
    }

    private ErrorBundle getNoProductsAvailableErrorBundle() {
        NoProductsAvailableException exception = new NoProductsAvailableException("No product available!");
        return new ErrorBundle(exception, exception.getMessage());
    }

    private ErrorBundle getProductNotFoundErrorBundle() {
        ProductNotFoundException exception = new ProductNotFoundException("Product not found!");
        return new ErrorBundle(exception, exception.getMessage());
    }

}