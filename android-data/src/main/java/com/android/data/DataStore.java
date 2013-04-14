package com.android.data;

import android.content.Context;
import android.util.Log;
import com.android.data.exceptions.DataException;
import com.couchbase.touchdb.TDDatabase;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.ektorp.TouchDBHttpClient;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ReplicationCommand;
import org.ektorp.android.util.EktorpAsyncTask;
import org.ektorp.impl.StdCouchDbInstance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private final Context context;

    public DataStore(final Context context, final String dbName) {
        this.context = context;
        this.dbName = dbName;
        server = getServer(context);
        httpClient = new TouchDBHttpClient(server);
        dbInstance = new StdCouchDbInstance(httpClient);
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

    public Context getContext() {
        return context;
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

    protected void replicate(String remoteDB, Set<String> userGroupsToReplicate) {
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
        };

        pushReplication.execute();

        updateDesignDocForReplication();

        HashMap<String, String> pullReplicationQueryParams = new HashMap<String, String>();
        try {
            pullReplicationQueryParams.put("forGroups", new ObjectMapper().writeValueAsString(userGroupsToReplicate));
            final ReplicationCommand pullCommand = new ReplicationCommand.Builder()
                    .source(remoteDB)
                    .target(dbName)
                    .continuous(true)
                    .filter("data/forGroups")
                    .queryParams(pullReplicationQueryParams)
                    .build();

            EktorpAsyncTask pullReplication = new EktorpAsyncTask() {
                @Override
                protected void doInBackground() {
                    dbInstance.replicate(pullCommand);
                }
            };

            pullReplication.execute();
        } catch (IOException e) {
            Log.e(getClass().getName(), "Pull Replication failed because of: " + e);
        }
    }

    private void updateDesignDocForReplication() {
        String designDocId = "_design/data";
        ObjectNode designDoc = connector.find(ObjectNode.class, designDocId);
        boolean isDesignDocExisting = (designDoc != null);
        if (!isDesignDocExisting) {
            designDoc = new ObjectNode(JsonNodeFactory.instance);
        }

        /**
         "forGroups"
         "function(doc, req) {
            var sharedWith = doc.forGroups;
            var requestFor = JSON.parse(req.query.forGroups);
            if((typeof sharedWith === "undefined") || (sharedWith.length === 0)) {
                return true;
            }
            for(var i = 0; i < requestFor.length; i++) {
                if(sharedWith.indexOf(requestFor[i]) != -1) {
                    return true;
                }
            }
            return false;
         }"
         */

        Map<String, String> filters = new HashMap<String, String>();
        filters.put("forGroups", "function(doc, req) {\n" +
                "            var sharedWith = doc.forGroups;\n" +
                "            var requestFor = JSON.parse(req.query.forGroups);\n" +
                "            if((typeof sharedWith === \"undefined\") || (sharedWith.length === 0)) {\n" +
                "                return true;\n" +
                "            }\n" +
                "            for(var i = 0; i < requestFor.length; i++) {\n" +
                "                if(sharedWith.indexOf(requestFor[i]) != -1) {\n" +
                "                    return true;\n" +
                "                }\n" +
                "            }\n" +
                "            return false;\n" +
                "         }");

        designDoc.putPOJO("filters", filters);
        if (isDesignDocExisting) {
            connector.update(designDoc);
        } else {
            connector.create(designDocId, designDoc);
        }
    }
}


