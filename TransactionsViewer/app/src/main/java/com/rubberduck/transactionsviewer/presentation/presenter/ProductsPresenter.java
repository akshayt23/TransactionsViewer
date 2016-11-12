package com.rubberduck.transactionsviewer.presentation.presenter;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;
import com.rubberduck.transactionsviewer.domain.model.Product;
import com.rubberduck.transactionsviewer.domain.usecase.GetAllProductsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.GetTransactionCountsUseCase;
import com.rubberduck.transactionsviewer.domain.usecase.UseCaseCallback;
import com.rubberduck.transactionsviewer.presentation.model.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class ProductsPresenter extends MvpPresenter<ProductsPresenter.View> {

    private final GetAllProductsUseCase getAllProductsUseCase;
    private final GetTransactionCountsUseCase getTransactionCountsUseCase;

    @Inject
    public ProductsPresenter(GetAllProductsUseCase getAllProductsUseCase,
                             GetTransactionCountsUseCase getTransactionCountsUseCase) {
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.getTransactionCountsUseCase = getTransactionCountsUseCase;
    }

    @Override
    public void attachView(View view) {
        super.attachView(view);
        getView().showProgressBar();
        getAllProductsUseCase.execute(new GetAllProductsCallback());
    }

    private final class GetAllProductsCallback implements UseCaseCallback<List<Product>> {

        @Override
        public void onSuccess(List<Product> products) {
            if (isViewAttached()) {
                List<String> productSkus = new ArrayList<>(products.size());
                for (Product product : products) {
                    productSkus.add(product.getSku());
                }

                getTransactionCountsUseCase
                        .init(productSkus)
                        .execute(new GetTransactionCountsCallback(products));
            }
        }

        @Override
        public void onFailure(ErrorBundle errorBundle) {
            if (isViewAttached()) {
                getView().hideProgressBar();
                getView().showError(errorBundle.getErrorMessage());
            }
        }
    }

    private final class GetTransactionCountsCallback implements UseCaseCallback<List<Integer>> {

        private final List<Product> products;

        GetTransactionCountsCallback(List<Product> products) {
            this.products = products;
        }

        @Override
        public void onSuccess(List<Integer> transactionCounts) {
            getView().hideProgressBar();

            List<ProductViewModel> productViewModels = new ArrayList<>(products.size());
            for (int i = 0; i < products.size(); i++) {
                ProductViewModel viewModel = new ProductViewModel(
                        products.get(i).getSku(),
                        String.format(Locale.US, "%d transactions", transactionCounts.get(i)));
                productViewModels.add(viewModel);
            }

            getView().showProducts(productViewModels);
        }

        @Override
        public void onFailure(ErrorBundle errorBundle) {
            getView().hideProgressBar();
            getView().showError(errorBundle.getErrorMessage());
        }
    }

    public interface View extends MvpPresenter.View {

        void showProgressBar();

        void hideProgressBar();

        void showProducts(List<ProductViewModel> products);

        void showError(String errorMessage);
    }
}
