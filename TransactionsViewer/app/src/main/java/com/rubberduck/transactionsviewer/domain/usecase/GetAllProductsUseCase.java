package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.exception.NoProductAvailableException;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.model.Product;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * A use case which gets all products from {@link ProductTransactionsRepository}
 */
public class GetAllProductsUseCase extends UseCase<List<Product>> {

    private final ProductTransactionsRepository repository;

    @Inject
    GetAllProductsUseCase(ThreadExecutor threadExecutor,
                                 MainThread mainThread,
                                 ProductTransactionsRepository repository) {
        super(threadExecutor, mainThread);
        this.repository = repository;
    }

    @Override
    void performUseCase(final UseCaseCallback<List<Product>> callback) {
        final List<Product> products;

        try {
            products = repository.getAllProducts();
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(products);
                }
            });
        } catch (final NoProductAvailableException e) {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(new ErrorBundle(e, e.getMessage()));
                }
            });
        }
    }
}
