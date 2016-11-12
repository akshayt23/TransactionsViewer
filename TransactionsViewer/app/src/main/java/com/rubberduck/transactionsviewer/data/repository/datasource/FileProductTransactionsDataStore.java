package com.rubberduck.transactionsviewer.data.repository.datasource;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.rubberduck.transactionsviewer.data.model.TransactionData;
import com.rubberduck.transactionsviewer.data.serializer.JsonSerializer;
import com.rubberduck.transactionsviewer.data.util.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ProductTransactionsDataStore} implementation that gets data from the local file system.
 */
class FileProductTransactionsDataStore implements ProductTransactionsDataStore {

    private final String TRANSACTIONS_JSON_FILE = "transactions.json";

    private final Context context;
    private final JsonSerializer jsonSerializer;

    FileProductTransactionsDataStore(Context context, JsonSerializer jsonSerializer) {
        this.context = context;
        this.jsonSerializer = jsonSerializer;
    }

    @Override
    public List<TransactionData> getAllTransactionsData() throws IOException {
        return readTransactionDataFromFile();
    }

    private List<TransactionData> readTransactionDataFromFile() throws IOException {
        String json = IOUtils.readJsonFromAsset(context, TRANSACTIONS_JSON_FILE);

        Type transactionDataListType = new TypeToken<ArrayList<TransactionData>>() {
        }.getType();
        return jsonSerializer.deserialize(json, transactionDataListType);
    }
}
