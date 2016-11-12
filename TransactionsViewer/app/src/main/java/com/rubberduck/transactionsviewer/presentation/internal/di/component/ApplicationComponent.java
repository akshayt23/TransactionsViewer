package com.rubberduck.transactionsviewer.presentation.internal.di.component;

import android.content.Context;

import com.rubberduck.transactionsviewer.domain.currencyconverter.CurrencyConverter;
import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;
import com.rubberduck.transactionsviewer.presentation.internal.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context applicationContext();

    ThreadExecutor threadExecutor();

    MainThread mainThread();

    ProductTransactionsRepository productTransactionsRepository();

    CurrencyConverter currencyConverter();
}
