package com.rubberduck.transactionsviewer.presentation.internal.di.module;

import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;
import com.rubberduck.transactionsviewer.domain.usecase.GetTransactionsUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class TransactionsModule {

    private final String productSku;

    public TransactionsModule(String productSku) {
        this.productSku = productSku;
    }

    @Provides
    GetTransactionsUseCase provideUseCase(ThreadExecutor threadExecutor,
                                          MainThread mainThread,
                                          ProductTransactionsRepository repository) {
        return new GetTransactionsUseCase(threadExecutor, mainThread, repository, productSku);
    }

}
