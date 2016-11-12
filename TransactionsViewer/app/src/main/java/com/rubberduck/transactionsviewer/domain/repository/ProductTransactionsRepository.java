package com.rubberduck.transactionsviewer.domain.repository;

import com.rubberduck.transactionsviewer.domain.exception.NoProductAvailableException;
import com.rubberduck.transactionsviewer.domain.exception.ProductNotFoundException;
import com.rubberduck.transactionsviewer.domain.model.Product;

import java.util.List;

/**
 * A repository that provides all the products and associated transactions in the system.
 */
public interface ProductTransactionsRepository {

    /**
     * Returns a list of all the {@link Product} in the system.
     *
     * @return A list of {@link Product}
     * @throws NoProductAvailableException If no products are available
     */
    List<Product> getAllProducts() throws NoProductAvailableException;

    /**
     * Returns the count of transactions for a particular {@link Product}.
     *
     * @param productSku The sku of the product whose transactions are required
     * @return The total no transactions
     * @throws ProductNotFoundException If a product with the given sku is not found
     */
    Integer getTransactionsCount(String productSku) throws ProductNotFoundException;

}
