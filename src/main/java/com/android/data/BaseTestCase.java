package com.android.data;

import android.test.InstrumentationTestCase;
import com.android.data.document_for_test.Task;

public class BaseTestCase extends InstrumentationTestCase {
    protected Repository<Task> taskRepository;
    private DataStore dataStore;

    @Override
    public void setUp() throws Exception {
        dataStore = new DataStore(getInstrumentation().getContext(), "test.db");
        taskRepository = new Repository<Task>(Task.class, dataStore);
        taskRepository.reset();
    }

    @Override
    protected void tearDown() throws Exception {
        dataStore.close();
        super.tearDown();
    }
}
