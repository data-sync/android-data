package com.android.data.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.android.data.DataStore;
import com.android.data.exceptions.DataException;
import com.couchbase.touchdb.TDDatabase;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.ektorp.TouchDBHttpClient;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;
import org.ektorp.CouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import java.io.IOException;

public class DataService extends Service {
    private final IBinder binder = new DataServiceBinder();
    private TDServer server;
    private TouchDBHttpClient httpClient;
    private DataStore dataStore;

    {
        TDURLStreamHandlerFactory.registerSelfIgnoreError();
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String dbName = getPackageName();

        try {
            server = new TDServer(getFilesDir().getAbsolutePath());
            httpClient = new TouchDBHttpClient(server);
            StdCouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector connector = dbInstance.createConnector(dbName, false);
            TDDatabase database = server.getDatabaseNamed(dbName);
            dataStore = new DataStore(connector, database);
        } catch (IOException e) {
            throw new DataException(e);
        }
    }

    @Override
    public void onDestroy() {
        if (httpClient != null) {
            httpClient.shutdown();
        }
        if (server != null) {
            server.close();
        }

        super.onDestroy();
    }

    public class DataServiceBinder extends Binder {
        public DataService getService() {
            return DataService.this;
        }
    }

    public DataStore getDataStore() {
        return dataStore;
    }
}
