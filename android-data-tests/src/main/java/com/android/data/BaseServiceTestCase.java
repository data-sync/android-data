package com.android.data;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

public class BaseServiceTestCase extends ServiceTestCase<DataService> {

    public BaseServiceTestCase() {
        super(DataService.class);
    }

    protected Intent getDataServiceIntent() {
        Intent intent = new Intent(getContext(), DataService.class);
        intent.putExtra(DataService.REMOTE_DB, DataService.DEFAULT_REMOTE_DB);
        return intent;
    }

    protected DataStore bindDataService() {
        IBinder binder = bindService(getDataServiceIntent());
        DataService service = ((DataService.DataServiceBinder) binder).getService();
        return service.getDataStore();
    }
}
