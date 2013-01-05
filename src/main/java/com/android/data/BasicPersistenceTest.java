package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.document_for_test.Task;

import java.util.Date;

public class BasicPersistenceTest extends BaseTestCase {
    @MediumTest
    public void testSaveDocument() {
        Task task = new Task("task1", new Date());
        repository.add(task);

        Task taskFromStore = repository.get(task.getId());
        assertEquals(task.getDescription(), taskFromStore.getDescription());
        assertEquals(task.getCreatedDate(), taskFromStore.getCreatedDate());
    }

    @MediumTest
    public void testUpdateDocument() {
        Task task = new Task("task1", new Date());
        repository.add(task);

        task.updateDescription("New Description");
        repository.update(task);

        Task taskFromStore = repository.get(task.getId());
        assertEquals("New Description", taskFromStore.getDescription());
    }

    @MediumTest
    public void testDeleteDocument() {
        Task task = new Task("task1", new Date());
        repository.add(task);

        repository.remove(task);

        assertFalse(repository.contains(task.getId()));
    }
}
