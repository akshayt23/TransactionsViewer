package com.rubberduck.transactionsviewer.data.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    public static String readJsonFromAsset(Context context, String assetName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = context.getAssets().open(assetName);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String str;
        while ((str = reader.readLine()) != null) {
            stringBuilder.append(str);
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }
}
