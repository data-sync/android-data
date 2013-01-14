package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.adapters.TaskListAdapter;
import com.android.data.models.Task;
import org.ektorp.android.util.CouchbaseViewListAdapter;

public class DataListAdapterTest extends BaseTestCase {

    private TaskListAdapter dataListAdapter;
    private Task task1;
    private CouchbaseViewListAdapter adapter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        task1 = new Task("Task1");
        taskRepository.add(task1);
        taskRepository.defineViewBy("description");
        dataListAdapter = new TaskListAdapter(taskRepository);
        sleepEnoughForAdapterToReQuery();
    }

    @MediumTest
    public void testGetItem() {
        assertEquals(1, dataListAdapter.getCount());
        assertEquals(task1, dataListAdapter.getItem(0));
    }

    @MediumTest
    public void testFollowChanges() throws InterruptedException {
        assertEquals(1, dataListAdapter.getCount());
        taskRepository.add(new Task("new task"));
        sleepEnoughForAdapterToReQuery();
        assertEquals(2, dataListAdapter.getCount());
    }
}
