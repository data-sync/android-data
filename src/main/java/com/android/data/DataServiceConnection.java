package com.android.data;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public abstract class DataServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        DataService service = ((DataService.DataServiceBinder) binder).getService();
        onDataConnection(service.getDataStore());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(getClass().getName(), "Service Disconnected");
    }

    abstract protected void onDataConnection(DataStore dataStore);
}
