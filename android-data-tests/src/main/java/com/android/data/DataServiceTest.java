package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;

public class DataServiceTest extends BaseServiceTestCase {

    @MediumTest
    public void testGetDataStoreFromService() {
        DataStore dataStore = bindDataService();
        Repository<Task> repository = new Repository<Task>(Task.class, dataStore);
        assertNotNull(dataStore);
        assertNotNull(repository);
    }
}
