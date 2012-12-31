package com.android.data;

import android.content.Context;
import com.android.data.exceptions.DataException;
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

    public DataStore(Context context, String dbName) {
        connector = getConnector(context, dbName);
    }

    CouchDbConnector getConnector() {
        return connector;
    }

    private CouchDbConnector getConnector(Context context, String dbName) {
            String filesDir = context.getFilesDir().getAbsolutePath();
        TDServer server = null;
        try {
            server = new TDServer(filesDir);
        } catch (IOException e) {
            throw new DataException(e);
        }
        TouchDBHttpClient httpClient = new TouchDBHttpClient(server);
        StdCouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        return dbInstance.createConnector(dbName, false);
    }
}
