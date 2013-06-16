package com.android.data;

import android.test.ActivityTestCase;
import android.test.InstrumentationTestCase;
import com.android.data.models.Task;

public class BaseTestCase extends ActivityTestCase {
    protected Repository<Task> taskRepository;
    protected DataStore dataStore;

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

    protected void sleepEnoughForChangesToBeFollowed() throws InterruptedException {
        Thread.sleep(500);
    }
}
