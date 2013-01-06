package com.android.data.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.android.data.DataStore;

public class DataService extends Service {
    private final IBinder binder = new DataServiceBinder();
    private DataStore dataStore;

    public IBinder onBind(Intent intent) {
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
