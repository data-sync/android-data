package com.android.data;

import android.R;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.TextView;
import com.android.data.document_for_test.Task;
import org.ektorp.android.util.CouchbaseViewListAdapter;

public class DataListAdapterTest extends BaseTestCase {

    private TaskListAdapter dataListAdapter;
    private Task task1;
    private CouchbaseViewListAdapter adapter;

    class TaskListAdapter extends DataListAdapter<Task> {
        public TaskListAdapter(Repository<Task> repository) {
            super(repository, "byDescription", R.layout.simple_list_item_1, true);
        }

        @Override
        View populateView(View view, Task document) {
            ((TextView) view).setText(document.getDescription());
            return view;
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        task1 = new Task("Task1");
        taskRepository.add(task1);
        taskRepository.defineViewBy("description");
        dataListAdapter = new TaskListAdapter(taskRepository);
        sleepEnoughForAdapterToRequery();
    }

    @MediumTest
    public void testGetItem() {
        assertEquals(1, dataListAdapter.getCount());
        assertEquals(task1, dataListAdapter.getDocument(0));
    }

    @MediumTest
    public void testFollowChanges() throws InterruptedException {
        assertEquals(1, dataListAdapter.getCount());
        taskRepository.add(new Task("new task"));
        sleepEnoughForAdapterToRequery();
        assertEquals(2, dataListAdapter.getCount());
    }

    private void sleepEnoughForAdapterToRequery() throws InterruptedException {
        Thread.sleep(1000);
    }
}
