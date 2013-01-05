package com.android.data;

import android.test.InstrumentationTestCase;
import com.android.data.document_for_test.Task;

public class BaseTestCase extends InstrumentationTestCase {
    protected Repository<Task> repository;
    private DataStore dataStore;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dataStore = new DataStore(getInstrumentation().getContext(), "test.db");
        repository = new Repository<Task>(Task.class, dataStore);
        repository.reset();
    }

    @Override
    protected void tearDown() throws Exception {
        dataStore.close();
        super.tearDown();
    }
}
