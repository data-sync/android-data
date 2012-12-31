package com.android.data;

import org.ektorp.support.CouchDbRepositorySupport;

public class Repository<T extends Document> extends CouchDbRepositorySupport<T> {
    protected Repository(Class<T> type, DataStore dataStore) {
        super(type, dataStore.getConnector());
    }
}