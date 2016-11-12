package com.rubberduck.transactionsviewer.data.repository.datasource;

import android.content.Context;

import com.rubberduck.transactionsviewer.data.serializer.JsonSerializer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProductTransactionsDataStoreFactory {

    private final Context context;
    private final JsonSerializer jsonSerializer;

    @Inject
    ProductTransactionsDataStoreFactory(Context context, JsonSerializer jsonSerializer) {
        this.context = context;
        this.jsonSerializer = jsonSerializer;
    }

    /**
     * Returns a local/remote data store depending on where the required data can be found.
     * For now, we'll just return a local implementation of {@link ProductTransactionsDataStore}.
     *
     * @return A {@link ProductTransactionsDataStore}
     */
    public ProductTransactionsDataStore create() {
        return new FileProductTransactionsDataStore(context, jsonSerializer);
    }
}
