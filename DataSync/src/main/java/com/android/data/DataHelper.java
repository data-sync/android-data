package com.android.data;

import com.android.data.exceptions.DataException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.impl.StdObjectMapperFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public class DataHelper {
    private static ObjectMapper mapper = new StdObjectMapperFactory().createObjectMapper();

    public static <T extends Document> T fromJson(final String json, final Class<T> cls) {
        return new IODataExceptionHandler<T>() {
            @Override
            protected T execute() throws IOException {
                return mapper.readValue(json, cls);
            }
        }.handle();
    }

    public static <T extends Document> T fromJson(final JsonNode jsonNode, final Class<T> cls) {
        return new IODataExceptionHandler<T>() {
            @Override
            protected T execute() throws IOException {
                return mapper.readValue(jsonNode, cls);
            }
        }.handle();
    }

    public static <T extends Document> String byTypeName(Class<T> cls) {
        return "by" + typeName(cls);
    }

    public static <T extends Document> String typeName(Class<T> cls) {
        return cls.getSimpleName();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Document> Class<T> getGenericClass(Class cls) {
        ParameterizedType parameterizedType = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
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
