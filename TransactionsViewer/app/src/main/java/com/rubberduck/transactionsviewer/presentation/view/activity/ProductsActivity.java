package com.rubberduck.transactionsviewer.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rubberduck.transactionsviewer.R;
import com.rubberduck.transactionsviewer.presentation.internal.di.component.DaggerProductComponent;
import com.rubberduck.transactionsviewer.presentation.internal.di.component.ProductComponent;
import com.rubberduck.transactionsviewer.presentation.model.ProductViewModel;
import com.rubberduck.transactionsviewer.presentation.presenter.ProductsPresenter;
import com.rubberduck.transactionsviewer.presentation.view.adapter.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ProductsActivity extends MvpActivity<ProductsPresenter>
        implements ProductsPresenter.View,
                   ProductsAdapter.OnProductSelectedListener {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.products_recycler)
    RecyclerView productsRecycler;

    @BindView(R.id.error_text_view)
    TextView errorTextView;

    @Inject
    ProductsPresenter productsPresenter;

    private ProductComponent productComponent;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Products");
        initProductsRecycler();
    }

    private void initProductsRecycler() {
        final List<ProductViewModel> emptyProductList = new ArrayList<>();

        productsAdapter = new ProductsAdapter(emptyProductList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        productsRecycler.setLayoutManager(linearLayoutManager);
        productsRecycler.setAdapter(productsAdapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_products;
    }

    @Override
    protected void initActivity(Bundle bundle) {
    }

    @Override
    protected void injectPresenter() {
        productComponent = DaggerProductComponent.builder()
                                                 .applicationComponent(getApplicationComponent())
                                                 .build();
        productComponent.inject(this);
    }

    @Override
    protected ProductsPresenter getPresenter() {
        return productsPresenter;
    }

    @Override
    public void onProductSelected(ProductViewModel product) {
        productsPresenter.onProductSelected(product.getSku());
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
    public void showProducts(List<ProductViewModel> products) {
        productsAdapter.refresh(products);
    }

    @Override
    public void showError(String errorMessage) {
        errorTextView.setText(errorMessage);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void openTransactionsScreen(String productSku) {
        Intent intent = TransactionsActivity.getCallingIntent(this, productSku);
        startActivity(intent);
    }
}
