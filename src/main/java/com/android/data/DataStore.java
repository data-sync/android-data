package com.android.data;

import android.content.Context;
import android.util.Log;
import com.android.data.exceptions.DataException;
import com.couchbase.touchdb.TDDatabase;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.ektorp.TouchDBHttpClient;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.ReplicationCommand;
import org.ektorp.android.util.EktorpAsyncTask;
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
    private final String dbName;
    private final CouchDbInstance dbInstance;

    public DataStore(final Context context, final String dbName, boolean isReplicationEnabled) {
        this.dbName = dbName;
        server = getServer(context);
        httpClient = new TouchDBHttpClient(server);
        dbInstance = new StdCouchDbInstance(httpClient);
        connector = dbInstance.createConnector(dbName, true);
        database = server.getDatabaseNamed(dbName);

        if (isReplicationEnabled) {
//            replicate("http://10.0.2.2:5984/data-test");
            replicate("http://couchdb.selvakn.in/data-test");
        }
    }

    public DataStore(final Context context, final String dbName) {
        this(context, dbName, true);
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

    private void replicate(String remoteDB) {
        final ReplicationCommand pushCommand = new ReplicationCommand.Builder()
                .source(dbName)
                .target(remoteDB)
                .continuous(true)
                .build();

        EktorpAsyncTask pushReplication = new EktorpAsyncTask() {
            @Override
            protected void doInBackground() {
                dbInstance.replicate(pushCommand);
            }

            @Override
            protected void onDbAccessException(DbAccessException dbAccessException) {
                Log.e(getClass().getName(), "DB exception: " + dbAccessException);
            }
        };

        pushReplication.execute();

        final ReplicationCommand pullCommand = new ReplicationCommand.Builder()
                .source(remoteDB)
                .target(dbName)
                .continuous(true)
                .build();

        EktorpAsyncTask pullReplication = new EktorpAsyncTask() {
            @Override
            protected void doInBackground() {
                dbInstance.replicate(pullCommand);
            }
        };

        pullReplication.execute();
    }
}