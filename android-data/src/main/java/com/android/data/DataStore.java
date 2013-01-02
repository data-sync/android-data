package com.android.data;

import com.couchbase.touchdb.TDDatabase;
import org.ektorp.CouchDbConnector;

public class DataStore {

    private final CouchDbConnector connector;
    private TDDatabase database;

    public DataStore(CouchDbConnector connector, TDDatabase database) {
        this.connector = connector;
        this.database = database;
    }

    CouchDbConnector getConnector() {
        return connector;
    }

    public TDDatabase getDatabase() {
        return database;
    }
}
