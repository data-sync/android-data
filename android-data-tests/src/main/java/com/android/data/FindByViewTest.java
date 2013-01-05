package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.document_for_test.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FindByViewTest extends BaseTestCase {
    @MediumTest
    public void testFindDocByView() throws IOException {
        repository.defineViewBy("description");
        Task task1 = new Task("Desc1");
        Task task2 = new Task("Desc2");
        repository.add(task1);
        repository.add(task2);

        List<Task> tasks = repository.byView("byDescription");
        assertEquals(2, tasks.size());
        assertEquals(Arrays.asList(task2, task1), tasks);
    }
}
