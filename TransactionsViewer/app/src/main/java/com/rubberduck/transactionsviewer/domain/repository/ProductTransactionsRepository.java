package com.rubberduck.transactionsviewer.domain.repository;

import com.rubberduck.transactionsviewer.domain.exception.NoProductAvailableException;
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
     * Returns a list of integers which contains the total transaction count for each {@link Product}
     *
     * @param products A list of {@link Product}
     * @return A list of integers
     */
    List<Integer> getTransactionCounts(List<Product> products);

}
