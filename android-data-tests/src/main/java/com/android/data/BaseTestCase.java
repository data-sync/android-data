package com.android.data;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import com.android.data.document_for_test.Task;
import com.android.data.services.DataService;

public class BaseTestCase extends ServiceTestCase<DataService> {
    protected Repository<Task> repository;

    public BaseTestCase() {
        super(DataService.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        IBinder binder = bindService(new Intent(getContext(), DataService.class));
        DataService service = ((DataService.DataServiceBinder) binder).getService();
        repository = new Repository<Task>(Task.class, service.getDataStore());
        repository.reset();
    }
}
