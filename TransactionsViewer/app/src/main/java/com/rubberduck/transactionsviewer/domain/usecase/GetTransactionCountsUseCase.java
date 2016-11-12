package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A use case which calculates the count of transactions each product in a given list of product SKUs.
 * If a given product sku is not found, the number of transactions for it is set to zero.
 * Call {@link #init(List)} before executing the use case.
 */
public class GetTransactionCountsUseCase extends UseCase<List<Integer>> {

    private final ProductTransactionsRepository repository;
    private List<String> productSkus;

    @Inject
    GetTransactionCountsUseCase(ThreadExecutor threadExecutor,
                                MainThread mainThread,
                                ProductTransactionsRepository repository) {
        super(threadExecutor, mainThread);
        this.repository = repository;
    }

    public GetTransactionCountsUseCase init(List<String> productSkus) {
        if (productSkus == null || productSkus.size() == 0) {
            throw new IllegalArgumentException("Products list cannot be null or empty");
        }

        this.productSkus = productSkus;

        return this;
    }

    @Override
    void performUseCase(final UseCaseCallback<List<Integer>> callback) {
        if (productSkus == null) {
            throw new IllegalArgumentException("Call init() before starting execution.");
        }

        final List<Integer> transactionCounts = new ArrayList<>(productSkus.size());
        for (String sku : productSkus) {
            try {
                transactionCounts.add(repository.getTransactionsCount(sku));
            } catch (ProductNotFoundException e) {
                transactionCounts.add(0);
            }
        }

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(transactionCounts);
            }
        });
    }
}
