package com.android.data;

abstract public class ContentObserver<T extends Document> {
    abstract public void onChange(T document);
}
