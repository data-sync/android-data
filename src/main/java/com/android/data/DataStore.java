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
    static {
        TDURLStreamHandlerFactory.registerSelfIgnoreError();
    }

    private final CouchDbConnector connector;
    private final TDDatabase database;
    private final TDServer server;
    private final TouchDBHttpClient httpClient;

    public DataStore(final Context context, final String dbName) {
        server = getServer(context);
        httpClient = new TouchDBHttpClient(server);
        final StdCouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
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

    protected CouchDbConnector getConnector() {
        return connector;
    }

    protected TDDatabase getDatabase() {
        return database;
    }

    private TDServer getServer(final Context context) {
        try {
            return new TDServer(getDBName(context));
        } catch (IOException e) {
            throw new DataException(e);
        }
    }

    private String getDBName(final Context context) {
        return context.getFilesDir().getAbsolutePath();
    }
}