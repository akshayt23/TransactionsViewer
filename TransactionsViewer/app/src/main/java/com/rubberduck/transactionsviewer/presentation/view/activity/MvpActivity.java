package com.rubberduck.transactionsviewer.presentation.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rubberduck.transactionsviewer.presentation.presenter.MvpPresenter;

/**
 * A base class for all activities that require a presenter.
 *
 * @param <P> The type of presenter that the activity works with.
 */
public abstract class MvpActivity<P extends MvpPresenter> extends AppCompatActivity implements MvpPresenter.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectPresenter();
        getPresenter().attachView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onViewForeground();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onViewBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().detachView();
    }

    protected abstract void injectPresenter();

    protected abstract P getPresenter();
}
