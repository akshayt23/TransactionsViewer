package com.rubberduck.transactionsviewer.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rubberduck.transactionsviewer.R;
import com.rubberduck.transactionsviewer.presentation.internal.di.component.DaggerTransactionsComponent;
import com.rubberduck.transactionsviewer.presentation.internal.di.component.TransactionsComponent;
import com.rubberduck.transactionsviewer.presentation.internal.di.module.TransactionsModule;
import com.rubberduck.transactionsviewer.presentation.model.TransactionViewModel;
import com.rubberduck.transactionsviewer.presentation.presenter.TransactionsPresenter;
import com.rubberduck.transactionsviewer.presentation.view.adapter.TransactionsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TransactionsActivity extends MvpActivity<TransactionsPresenter>
        implements TransactionsPresenter.View {

    private static final String PARAM_PRODUCT_SKU = "product_sku";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.total_amount_text_view)
    TextView totalAmountTextView;

    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;

    @BindView(R.id.error_text_view)
    TextView errorTextView;

    @Inject
    TransactionsPresenter transactionsPresenter;

    private TransactionsComponent transactionsComponent;
    private TransactionsAdapter transactionsAdapter;
    private String productSku;

    public static Intent getCallingIntent(Context context, String productSku) {
        Intent intent = new Intent(context, TransactionsActivity.class);
        intent.putExtra(PARAM_PRODUCT_SKU, productSku);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(productSku);
        initProductsRecycler();
    }

    private void initProductsRecycler() {
        final List<TransactionViewModel> emptyTransactionsList = new ArrayList<>();

        transactionsAdapter = new TransactionsAdapter(emptyTransactionsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        transactionsRecycler.setLayoutManager(linearLayoutManager);
        transactionsRecycler.setAdapter(transactionsAdapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_transactions;
    }

    @Override
    protected void initActivity(Bundle bundle) {
        productSku = bundle.getString(PARAM_PRODUCT_SKU);
    }

    @Override
    protected void injectPresenter() {
        initInjector();
        transactionsComponent.inject(this);
    }

    private void initInjector() {
        transactionsComponent = DaggerTransactionsComponent
                .builder()
                .applicationComponent(getApplicationComponent())
                .transactionsModule(new TransactionsModule(productSku))
                .build();
    }

    @Override
    protected TransactionsPresenter getPresenter() {
        return transactionsPresenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PARAM_PRODUCT_SKU, productSku);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showTotalAmount(String totalAmount) {
        totalAmountTextView.setText(totalAmount);
        totalAmountTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTransactions(List<TransactionViewModel> transactions) {
        transactionsAdapter.refreshTransactions(transactions);
    }

    @Override
    public void showError(String errorMessage) {
        errorTextView.setText(errorMessage);
        errorTextView.setVisibility(View.VISIBLE);
    }
}