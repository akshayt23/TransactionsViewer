package com.rubberduck.transactionsviewer.data.serializer;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A helper class that helps with conversion of POJOs to json and vice-versa.
 */
@Singleton
public class JsonSerializer {

    private final Gson gson = new Gson();

    @Inject
    public JsonSerializer() {
    }

    /**
     * Serialize an object to its equivalent Json format.
     *
     * @param source       The object for which JSON representation is to be created
     * @param typeOfSource The specific genericized type of source
     * @return Json representation of source
     */
    public String serialize(Object source, Type typeOfSource) {
        return gson.toJson(source, typeOfSource);
    }

    /**
     * This method deserializes the specified Json into an object of the specified class. Use this
     * method for generic type objects.
     *
     * @param json    The string from which the object is to be deserialized
     * @param typeOfT The specific genericized type
     * @param <T>     The type of the desired object
     * @return An object of type T from the json string
     */
    public <T> T deserialize(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
}
