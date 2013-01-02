package com.android.data;

import android.content.Context;
import com.android.data.exceptions.DataException;
import com.couchbase.touchdb.TDDatabase;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.ektorp.TouchDBHttpClient;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;
import org.ektorp.CouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import java.io.IOException;

public class DataStore {
    {
        TDURLStreamHandlerFactory.registerSelfIgnoreError();
    }

    private final CouchDbConnector connector;
    private final TDDatabase database;
    private TDServer server;
    private TouchDBHttpClient httpClient;

    public DataStore(Context context, String dbName) {
        server = getServer(context);
        httpClient = new TouchDBHttpClient(server);
        StdCouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        connector = dbInstance.createConnector(dbName, true);
        database = server.getDatabaseNamed(dbName);
    }

    public void close() {
        if (httpClient != null) {
            httpClient.shutdown();
        }
        if (server != null) {
            server.close();
        }
    }

    public CouchDbConnector getConnector() {
        return connector;
    }

    public TDDatabase getDatabase() {
        return database;
    }

    private TDServer getServer(Context context) {
        try {
            return new TDServer(context.getFilesDir().getAbsolutePath());
        } catch (IOException e) {
            throw new DataException(e);
        }
    }
}