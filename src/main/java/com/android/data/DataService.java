package com.android.data;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.HashSet;
import java.util.Set;

public class DataService extends Service {
    public static final String GROUPS = "GROUPS";
    public static final String REMOTE_DB = "REMOTE_DB";
    public static final String DEFAULT_REMOTE_DB = "http://10.0.2.2:5984/data-test";

    private final IBinder binder = new DataServiceBinder();
    private DataStore dataStore;
    private boolean isReplicationRunning = false;

    public IBinder onBind(Intent intent) {
        if (!isReplicationRunning) {
            @SuppressWarnings("unchecked")
            Set<String> groups = (HashSet<String>) intent.getSerializableExtra(GROUPS);
            String remoteDB = intent.getExtras().getString(REMOTE_DB, DEFAULT_REMOTE_DB);
            dataStore.replicate(remoteDB, groups);
            isReplicationRunning = true;
        }
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String dbName = getPackageName();
        dataStore = new DataStore(this, dbName);
    }

    @Override
    public void onDestroy() {
        if (dataStore != null) {
            dataStore.close();
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
