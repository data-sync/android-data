package com.android.data;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.services.DataService;

public class PersistenceTest extends ServiceTestCase<DataService> {

    private Repository<TestDocument> repository;

    public PersistenceTest() {
        super(DataService.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        IBinder binder = bindService(new Intent(getContext(), DataService.class));
        DataService service = ((DataService.DataServiceBinder) binder).getService();
        repository = new Repository<TestDocument>(TestDocument.class, service.getDataStore());
    }

    @MediumTest
    public void testSaveData() {
        TestDocument data = new TestDocument(1, 2);
        repository.add(data);

        TestDocument actualData = repository.get(data.getId());
        assertEquals(data.getAttr1(), actualData.getAttr1());
        assertEquals(data.getAttr2(), actualData.getAttr2());
    }

    @MediumTest
    public void testUpdateData() {
        TestDocument data = new TestDocument(1, 2);
        repository.add(data);

        data.setAttr1(3);
        repository.update(data);

        TestDocument actualData = repository.get(data.getId());
        assertEquals(3, actualData.getAttr1());
    }
}
