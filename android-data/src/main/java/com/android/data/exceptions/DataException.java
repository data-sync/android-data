package com.android.data.exceptions;

public class DataException extends RuntimeException {
    public DataException(final Exception exception) {
        super(exception);
    }

    public DataException(String message, final Exception exception) {
        super(message, exception);
    }
}
