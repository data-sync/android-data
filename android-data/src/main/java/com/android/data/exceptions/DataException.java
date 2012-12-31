package com.android.data.exceptions;

public class DataException extends RuntimeException {
    public DataException(Exception exception) {
        super(exception);
    }
}
