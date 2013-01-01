package com.android.data;

import org.ektorp.CouchDbConnector;

public class DataStore {

    private final CouchDbConnector connector;

    public DataStore(CouchDbConnector connector) {
        this.connector = connector;
    }

    CouchDbConnector getConnector() {
        return connector;
    }
}
