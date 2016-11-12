package com.rubberduck.transactionsviewer.data.repository.datasource;

import com.rubberduck.transactionsviewer.data.model.TransactionData;

import java.io.IOException;
import java.util.List;

/**
 * A data store that provides the list of all products and associated transactions in the system.
 */
public interface ProductTransactionsDataStore {

    /**
     * Returns a list of all transactions and associated products.
     */
    List<TransactionData> getAllTransactionsData() throws IOException;
}
