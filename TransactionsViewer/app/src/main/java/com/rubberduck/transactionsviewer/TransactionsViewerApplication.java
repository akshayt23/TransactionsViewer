package com.rubberduck.transactionsviewer;

import android.app.Application;

import com.rubberduck.transactionsviewer.presentation.internal.di.component.ApplicationComponent;
import com.rubberduck.transactionsviewer.presentation.internal.di.component.DaggerApplicationComponent;
import com.rubberduck.transactionsviewer.presentation.internal.di.module.ApplicationModule;

public class TransactionsViewerApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
    }

    private void initInjector() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
