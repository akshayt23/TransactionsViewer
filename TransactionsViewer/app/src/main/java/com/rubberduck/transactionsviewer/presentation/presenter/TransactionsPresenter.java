package com.rubberduck.transactionsviewer.presentation.presenter;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.model.Amount;
import com.rubberduck.transactionsviewer.domain.model.Transaction;
import com.rubberduck.transactionsviewer.domain.usecase.ConvertAmountsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.GetTransactionsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.UseCaseCallback;
import com.rubberduck.transactionsviewer.presentation.model.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class TransactionsPresenter extends MvpPresenter<TransactionsPresenter.View> {

    private final GetTransactionsUseCase getTransactionsUseCase;
    private final ConvertAmountsUseCase convertAmountsUseCase;

    @Inject
    TransactionsPresenter(GetTransactionsUseCase getTransactionsUseCase,
                          ConvertAmountsUseCase convertAmountsUseCase) {
        this.getTransactionsUseCase = getTransactionsUseCase;
        this.convertAmountsUseCase = convertAmountsUseCase;
    }

    @Override
    public void attachView(View view) {
        super.attachView(view);
        getView().showProgressBar();
        getTransactionsUseCase.execute(new GetTransactionsCallback());
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    private final class GetTransactionsCallback implements UseCaseCallback<List<Transaction>> {

        @Override
        public void onSuccess(List<Transaction> transactions) {
            List<Amount> originalAmounts = new ArrayList<>(transactions.size());
            for (Transaction transaction : transactions) {
                originalAmounts.add(transaction.getAmount());
            }

            convertAmountsUseCase.init(originalAmounts, "GBP")
                                 .execute(new ConvertAmountsCallback(originalAmounts));
        }

        @Override
        public void onFailure(ErrorBundle errorBundle) {
            if (isViewAttached()) {
                getView().hideProgressBar();
                getView().showError(errorBundle.getErrorMessage());
            }
        }
    }

    private final class ConvertAmountsCallback implements UseCaseCallback<List<Amount>> {

        private final List<Amount> fromAmounts;

        ConvertAmountsCallback(List<Amount> fromAmounts) {
            this.fromAmounts = fromAmounts;
        }

        @Override
        public void onSuccess(List<Amount> resultAmounts) {
            getView().hideProgressBar();

            List<TransactionViewModel> transactionViewModels = new ArrayList<>(fromAmounts.size());
            for (int i = 0; i < fromAmounts.size(); i++) {
                String originalAmount = String.format("%s %s",
                                                      fromAmounts.get(i).getCurrency(),
                                                      fromAmounts.get(i).getValue());
                String convertedAmount = resultAmounts.get(i) != null
                        ? String.format(Locale.US, "%s %.2f",
                                        resultAmounts.get(i).getCurrency(),
                                        resultAmounts.get(i).getValue())
                        : "Unknown conversion rate to GBP";

                transactionViewModels.add(new TransactionViewModel(originalAmount, convertedAmount));
            }

            getView().showTransactions(transactionViewModels);
        }

        @Override
        public void onFailure(ErrorBundle errorBundle) {
            // Do nothing.
        }
    }

    public interface View extends MvpPresenter.View {

        void showProgressBar();

        void hideProgressBar();

        void showTransactions(List<TransactionViewModel> transactions);

        void showError(String errorMessage);
    }
}