package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.model.Transaction;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * A use case which gets all the transactions for a given product SKU.
 */
public class GetTransactionsUseCase extends UseCase<List<Transaction>> {

    private final ProductTransactionsRepository repository;
    private final String productSku;

    @Inject
    public GetTransactionsUseCase(ThreadExecutor threadExecutor,
                                  MainThread mainThread,
                                  ProductTransactionsRepository repository,
                                  String productSku) {
        super(threadExecutor, mainThread);
        this.repository = repository;
        this.productSku = productSku;
    }

    @Override
    void performUseCase(final UseCaseCallback<List<Transaction>> callback) {
        final List<Transaction> transactions;

        try {
            transactions = repository.getAllTransactions(productSku);
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(transactions);
                }
            });

        } catch (final ProductNotFoundException e) {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(new ErrorBundle(e, e.getMessage()));
                }
            });

        }
    }
}