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

    private final String CONVERT_TO_CURRENCY = "GBP";

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

            convertAmountsUseCase.init(originalAmounts, CONVERT_TO_CURRENCY)
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

            boolean allAmountsConverted = true;
            double totalAmount = 0;
            List<TransactionViewModel> transactionViewModels = new ArrayList<>(fromAmounts.size());

            for (int i = 0; i < fromAmounts.size(); i++) {
                String convertedAmount = resultAmounts.get(i) != null
                        ? getDisplayString(resultAmounts.get(i))
                        : String.format("Unknown conversion rate to %s", CONVERT_TO_CURRENCY);

                transactionViewModels.add(new TransactionViewModel(
                        getDisplayString(fromAmounts.get(i)), convertedAmount));

                if (resultAmounts.get(i) != null) {
                    totalAmount += resultAmounts.get(i).getValue();
                } else {
                    allAmountsConverted = false;
                }
            }

            if (allAmountsConverted) {
                getView().showTotalAmount(String.format(Locale.US, "Total: %.2f GBP", totalAmount));
            } else {
                getView().showTotalAmount(String.format("Failed to convert at least one amount to %s", CONVERT_TO_CURRENCY));
            }
            getView().showTransactions(transactionViewModels);
        }

        private String getDisplayString(Amount amount) {
            return String.format(Locale.US, "%s %.2f", amount.getCurrency(), amount.getValue());
        }

        @Override
        public void onFailure(ErrorBundle errorBundle) {
            // Do nothing.
        }
    }

    public interface View extends MvpPresenter.View {

        void showProgressBar();

        void hideProgressBar();

        void showTotalAmount(String totalAmount);

        void showTransactions(List<TransactionViewModel> transactions);

        void showError(String errorMessage);
    }
}