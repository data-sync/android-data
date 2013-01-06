package com.android.data.exceptions;

public class DataException extends RuntimeException {
    public DataException(final Exception exception) {
        super(exception);
    }
}
