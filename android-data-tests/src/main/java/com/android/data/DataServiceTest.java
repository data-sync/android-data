package com.android.data;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.document_for_test.Task;
import com.android.data.services.DataService;

public class DataServiceTest extends ServiceTestCase<DataService> {
    public DataServiceTest() {
        super(DataService.class);
    }

    @MediumTest
    public void testGetDataStoreFromService() {
        IBinder binder = bindService(new Intent(getContext(), DataService.class));
        DataService service = ((DataService.DataServiceBinder) binder).getService();
        DataStore dataStore = service.getDataStore();
        Repository<Task> repository = new Repository<Task>(Task.class, dataStore);
        assertNotNull(dataStore);
        assertNotNull(repository);
    }
}
