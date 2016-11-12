package com.rubberduck.transactionsviewer.presentation.internal.di.module;

import android.content.Context;

import com.rubberduck.transactionsviewer.TransactionsViewerApplication;
import com.rubberduck.transactionsviewer.data.executor.JobExecutor;
import com.rubberduck.transactionsviewer.data.repository.ProductTransactionsDataRepository;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;
import com.rubberduck.transactionsviewer.presentation.view.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

    private final TransactionsViewerApplication application;

    public ApplicationModule(TransactionsViewerApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    MainThread provideMainThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    ProductTransactionsRepository provideRepository(ProductTransactionsDataRepository repository) {
        return repository;
    }
}
