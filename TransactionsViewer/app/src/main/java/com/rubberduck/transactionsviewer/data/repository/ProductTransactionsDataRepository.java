package com.rubberduck.transactionsviewer.data.repository;

import com.rubberduck.transactionsviewer.data.model.TransactionData;
import com.rubberduck.transactionsviewer.data.repository.datasource.ProductTransactionsDataStore;
import com.rubberduck.transactionsviewer.data.repository.datasource.ProductTransactionsDataStoreFactory;
import com.rubberduck.transactionsviewer.domain.exception.NoProductsAvailableException;
import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.model.Amount;
import com.rubberduck.transactionsviewer.domain.model.Product;
import com.rubberduck.transactionsviewer.domain.model.Transaction;
import com.rubberduck.transactionsviewer.domain.repository.ProductTransactionsRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProductTransactionsDataRepository implements ProductTransactionsRepository {

    private final ProductTransactionsDataStoreFactory dataStoreFactory;

    private Map<String, Product> skuProductMap;
    private Map<Product, List<Transaction>> productTransactionsMap;

    @Inject
    ProductTransactionsDataRepository(ProductTransactionsDataStoreFactory dataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
    }

    @Override
    public List<Product> getAllProducts() throws NoProductsAvailableException {
        if (skuProductMap == null || productTransactionsMap == null) {
            try {
                populateData(dataStoreFactory.create());
            } catch (IOException e) {
                throw new NoProductsAvailableException("Failed to read data from json.");
            }
        }

        if (productTransactionsMap.isEmpty()) {
            throw new NoProductsAvailableException("No products available.");
        }

        return new ArrayList<>(productTransactionsMap.keySet());
    }

    @Override
    public Integer getTransactionsCount(String productSku) throws ProductNotFoundException {
        if (skuProductMap == null || productTransactionsMap == null) {
            try {
                populateData(dataStoreFactory.create());
            } catch (IOException e) {
                throw new ProductNotFoundException("Failed to read data from json.");
            }
        }

        if (productSku == null || !skuProductMap.containsKey(productSku)) {
            throw new ProductNotFoundException(String.format("Can't find product with sku: %s", productSku));
        }

        Product product = skuProductMap.get(productSku);

        List<Transaction> transactions;
        if (!productTransactionsMap.containsKey(product)) {
            transactions = new ArrayList<>();
        } else {
            transactions = productTransactionsMap.get(product);
        }

        return transactions.size();
    }

    private void populateData(ProductTransactionsDataStore dataStore) throws IOException {
        skuProductMap = new HashMap<>();
        productTransactionsMap = new HashMap<>();

        List<TransactionData> transactions = dataStore.getAllTransactionsData();
        for (TransactionData transactionData : transactions) {
            Product product = new Product(transactionData.getSku());
            addProductToMap(product);

            Amount amount = new Amount(transactionData.getCurrency(), transactionData.getAmount());
            Transaction transaction = new Transaction(product, amount);
            addTransactionToMap(product, transaction);
        }
    }

    private void addProductToMap(Product product) {
        skuProductMap.put(product.getSku(), product);
    }

    private void addTransactionToMap(Product product, Transaction transaction) {
        List<Transaction> transactionsForProduct = productTransactionsMap.get(product);

        if (transactionsForProduct == null) {
            transactionsForProduct = new ArrayList<>();
        }
        transactionsForProduct.add(transaction);

        productTransactionsMap.put(product, transactionsForProduct);
    }
}
