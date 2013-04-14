package com.android.data;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;

public class DataServiceTest extends ServiceTestCase<DataService> {
    public DataServiceTest() {
        super(DataService.class);
    }

    @MediumTest
    public void testGetDataStoreFromService() {
        Intent intent = new Intent(getContext(), DataService.class);
        intent.putExtra(DataService.REMOTE_DB, DataService.DEFAULT_REMOTE_DB);
        IBinder binder = bindService(intent);
        DataService service = ((DataService.DataServiceBinder) binder).getService();
        DataStore dataStore = service.getDataStore();
        Repository<Task> repository = new Repository<Task>(Task.class, dataStore);
        assertNotNull(dataStore);
        assertNotNull(repository);
    }
}
