package com.rubberduck.transactionsviewer.presentation.presenter;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.model.Amount;
import com.rubberduck.transactionsviewer.domain.model.Product;
import com.rubberduck.transactionsviewer.domain.model.Transaction;
import com.rubberduck.transactionsviewer.domain.usecase.ConvertAmountsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.GetTransactionsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.UseCaseCallback;
import com.rubberduck.transactionsviewer.presentation.model.TransactionViewModel;

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

public class TransactionsPresenterTest {

    @Mock
    private TransactionsPresenter.View transactionsView;
    @Mock
    private GetTransactionsUseCase getTransactionsUseCase;
    @Mock
    private ConvertAmountsUseCase convertAmountsUseCase;

    @Captor
    private ArgumentCaptor<UseCaseCallback<List<Transaction>>> getTransactionsCallbackCaptor;
    @Captor
    private ArgumentCaptor<UseCaseCallback<List<Amount>>> convertAmountsCallbackCaptor;

    private List<Amount> dummyAmounts;
    private List<Amount> dummyAmountsInGbp;
    private List<Transaction> dummyTransactions;
    private List<TransactionViewModel> dummyTransactionViewModels;

    private TransactionsPresenter transactionsPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        transactionsPresenter = new TransactionsPresenter(getTransactionsUseCase, convertAmountsUseCase);

        dummyAmounts = Arrays.asList(new Amount("USD", 30.6), new Amount("GBP", 23.7), new Amount("CAD", 29.3));

        dummyAmountsInGbp = Arrays.asList(new Amount("GBP", 23.5687),
                                          new Amount("GBP", 23.7),
                                          new Amount("GBP", 20.762));

        dummyTransactions = Arrays.asList(new Transaction(new Product("Sku1"), dummyAmounts.get(0)),
                                          new Transaction(new Product("Sku2"), dummyAmounts.get(1)),
                                          new Transaction(new Product("Sku3"), dummyAmounts.get(2)));

        dummyTransactionViewModels = Arrays.asList(new TransactionViewModel("USD 30.60", "GBP 23.57"),
                                                   new TransactionViewModel("GBP 23.70", "GBP 23.70"),
                                                   new TransactionViewModel("CAD 29.30", "GBP 20.76"));

        when(convertAmountsUseCase.init(dummyAmounts, "GBP")).thenReturn(convertAmountsUseCase);
    }

    @Test
    public void showProgressBarAndExecuteGetTransactionsUseCaseOnViewAttach() {
        transactionsPresenter.attachView(transactionsView);

        verify(transactionsView).showProgressBar();
        verify(getTransactionsUseCase).execute(ArgumentMatchers.<UseCaseCallback<List<Transaction>>>any());
    }

    @Test
    public void hideProgressBarAndShowErrorIfTransactionsRetrievalFails() {
        transactionsPresenter.attachView(transactionsView);

        verify(getTransactionsUseCase).execute(getTransactionsCallbackCaptor.capture());
        getTransactionsCallbackCaptor.getValue().onFailure(getProductNotFoundErrorBundle());

        verify(transactionsView).hideProgressBar();
        verify(transactionsView).showError("Can't find product with sku: Sku1");
    }

    @Test
    public void hideProgressBarAndShowTransactionsAfterConversionToGbp() {
        transactionsPresenter.attachView(transactionsView);

        verify(getTransactionsUseCase).execute(getTransactionsCallbackCaptor.capture());
        getTransactionsCallbackCaptor.getValue().onSuccess(dummyTransactions);

        verify(convertAmountsUseCase).init(dummyAmounts, "GBP");
        verify(convertAmountsUseCase).execute(convertAmountsCallbackCaptor.capture());
        convertAmountsCallbackCaptor.getValue().onSuccess(dummyAmountsInGbp);

        verify(transactionsView).hideProgressBar();
        verify(transactionsView).showTransactions(dummyTransactionViewModels);
    }

    private ErrorBundle getProductNotFoundErrorBundle() {
        ProductNotFoundException exception = new ProductNotFoundException(
                "Can't find product with sku: " + dummyTransactions.get(0).getProduct().getSku());
        return new ErrorBundle(exception, exception.getMessage());
    }

}