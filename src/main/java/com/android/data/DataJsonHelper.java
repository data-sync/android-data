package com.android.data;

import com.android.data.exceptions.DataException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class DataJsonHelper {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(final String json, final Class<T> cls) {
        return new IODataExceptionHandler<T>() {
            @Override
            protected T execute() throws IOException {
                return mapper.readValue(json, cls);
            }
        }.handle();
    }

    public static <T> T fromJson(final JsonNode jsonNode, final Class<T> cls) {
        return new IODataExceptionHandler<T>() {
            @Override
            protected T execute() throws IOException {
                return mapper.readValue(jsonNode, cls);
            }
        }.handle();
    }

    private static abstract class IODataExceptionHandler<T> {
        protected abstract T execute() throws IOException;

        protected T handle() {
            try {
                return execute();
            } catch (IOException e) {
                throw new DataException("Not able to de-serialize", e);
            }
        }
    }
}
