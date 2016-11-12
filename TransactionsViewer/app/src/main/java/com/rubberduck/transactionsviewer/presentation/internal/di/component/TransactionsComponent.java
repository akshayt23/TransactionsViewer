package com.rubberduck.transactionsviewer.presentation.internal.di.component;

import com.rubberduck.transactionsviewer.presentation.internal.di.ProductScope;
import com.rubberduck.transactionsviewer.presentation.internal.di.module.TransactionsModule;
import com.rubberduck.transactionsviewer.presentation.view.activity.TransactionsActivity;

import dagger.Component;

@ProductScope
@Component(dependencies = ApplicationComponent.class, modules = TransactionsModule.class)
public interface TransactionsComponent {

    void inject(TransactionsActivity activity);
}
