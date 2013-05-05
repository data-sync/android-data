package com.android.data;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.android.data.notification.NotificationDocument;
import com.android.data.notification.NotificationObserver;

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
        String deviceId = Device.register(dataStore);
        if (!isReplicationRunning) {
            @SuppressWarnings("unchecked")
            Set<String> groups = getGroupsFromIntent(intent);
            groups.add(deviceId);

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
        listenForNotifications();
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

    private void listenForNotifications() {
        Repository<NotificationDocument> notificationRepo = new Repository<NotificationDocument>(NotificationDocument.class, dataStore);
        NotificationObserver notificationObserver = new NotificationObserver(this);
        notificationRepo.registerContentObserver(notificationObserver.changesCommandToFollow(), notificationObserver);
    }

    private Set<String> getGroupsFromIntent(Intent intent) {
        Set<String> groups = (HashSet<String>) intent.getSerializableExtra(GROUPS);
        if(groups == null) {
            groups = new HashSet<String>();
        }
        return groups;
    }
}
