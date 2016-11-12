package com.rubberduck.transactionsviewer.presentation.internal.di.component;

import com.rubberduck.transactionsviewer.presentation.internal.di.ActivityScope;
import com.rubberduck.transactionsviewer.presentation.view.activity.ProductsActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ProductComponent {

    void inject(ProductsActivity activity);
}
