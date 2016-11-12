package com.rubberduck.transactionsviewer.data.currencyconverter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.rubberduck.transactionsviewer.data.model.CurrencyConversionData;
import com.rubberduck.transactionsviewer.data.serializer.JsonSerializer;
import com.rubberduck.transactionsviewer.data.util.IOUtils;
import com.rubberduck.transactionsviewer.domain.currencyconverter.CurrencyConverter;
import com.rubberduck.transactionsviewer.domain.exception.CurrencyConversionFailedException;
import com.rubberduck.transactionsviewer.domain.model.Amount;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * An implementation of a {@link CurrencyConverter} that gets the conversion rates from the local filesystem.
 */
@Singleton
public class LocalCurrencyConverter implements CurrencyConverter {

    private static final String CONVERSION_RATES_JSON_FILE = "rates.json";

    private final Context context;
    private final JsonSerializer jsonSerializer;

    private DirectedGraph<String, Double> conversionRatesGraph;

    @Inject
    LocalCurrencyConverter(Context context, JsonSerializer jsonSerializer) {
        this.context = context;
        this.jsonSerializer = jsonSerializer;
    }

    @Override
    public Amount convert(Amount source, String toCurrency) throws CurrencyConversionFailedException {
        if (source == null || toCurrency == null) {
            throw new IllegalArgumentException("Can't convert null values.");
        }

        if (conversionRatesGraph == null) {
            try {
                initConversionRates();
            } catch (IOException e) {
                throw new CurrencyConversionFailedException("Failed to read conversion rates from file.");
            }
        }

        Double conversionRate;
        if (conversionRatesGraph.containsEdge(source.getCurrency(), toCurrency)) {
            conversionRate = conversionRatesGraph.getEdge(source.getCurrency(), toCurrency);
        } else {
            conversionRate = calculateConversionRate(source.getCurrency(), toCurrency);
            conversionRatesGraph.addEdge(source.getCurrency(), toCurrency, conversionRate);
        }

        return new Amount(toCurrency, source.getValue() * conversionRate);
    }

    private void initConversionRates() throws IOException {
        conversionRatesGraph = new DefaultDirectedGraph<>(Double.class);

        List<CurrencyConversionData> conversions = readConversionRatesFromFile();
        for (CurrencyConversionData currencyConversionData : conversions) {
            addConversionRateToGraph(
                    currencyConversionData.getFrom(),
                    currencyConversionData.getTo(),
                    currencyConversionData.getRate());
        }
    }

    private List<CurrencyConversionData> readConversionRatesFromFile() throws IOException {
        String json = IOUtils.readJsonFromAsset(context, CONVERSION_RATES_JSON_FILE);

        Type conversionDataListType = new TypeToken<ArrayList<CurrencyConversionData>>() {
        }.getType();
        return jsonSerializer.deserialize(json, conversionDataListType);
    }

    private void addConversionRateToGraph(String fromCurrency, String toCurrency, Double rate) {
        if (!conversionRatesGraph.containsVertex(fromCurrency)) {
            conversionRatesGraph.addVertex(fromCurrency);
        }

        if (!conversionRatesGraph.containsVertex(toCurrency)) {
            conversionRatesGraph.addVertex(toCurrency);
        }

        conversionRatesGraph.addEdge(fromCurrency, toCurrency, rate);
    }

    private Double calculateConversionRate(String from, String to) throws CurrencyConversionFailedException {
        List<Double> path = DijkstraShortestPath.findPathBetween(conversionRatesGraph, from, to);

        if (path == null) {
            throw new CurrencyConversionFailedException("Conversion rate not known.");
        }

        Double conversionRate = 1.0;
        for (Double rate : path) {
            conversionRate *= rate;
        }

        return conversionRate;
    }
}