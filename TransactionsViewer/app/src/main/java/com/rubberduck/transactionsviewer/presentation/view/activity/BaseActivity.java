package com.rubberduck.transactionsviewer.presentation.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rubberduck.transactionsviewer.TransactionsViewerApplication;
import com.rubberduck.transactionsviewer.presentation.internal.di.component.ApplicationComponent;

import butterknife.ButterKnife;

/**
 * A base class for all activities.
 * Handles inflating the layout file and other initialization.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            if (getIntent().getExtras() != null) {
                initActivity(getIntent().getExtras());
            }
        } else {
            initActivity(savedInstanceState);
        }
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((TransactionsViewerApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Return the layout resource id to be inflated.
     */
    protected abstract int getLayoutResourceId();

    /**
     * Initialize activity by reading values from the provided bundle.
     */
    protected abstract void initActivity(Bundle bundle);
}
