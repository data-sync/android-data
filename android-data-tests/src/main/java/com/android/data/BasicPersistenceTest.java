package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;

import java.util.Date;

public class BasicPersistenceTest extends BaseTestCase {
    @MediumTest
    public void testSaveDocument() {
        Task task = new Task("task1", new Date());
        taskRepository.add(task);

        Task taskFromStore = taskRepository.get(task.getId());
        assertEquals(task.getDescription(), taskFromStore.getDescription());
        assertEquals(task.getCreatedDate(), taskFromStore.getCreatedDate());
    }

    @MediumTest
    public void testUpdateDocument() {
        Task task = new Task("task1", new Date());
        taskRepository.add(task);

        task.updateDescription("New Description");
        taskRepository.update(task);

        Task taskFromStore = taskRepository.get(task.getId());
        assertEquals("New Description", taskFromStore.getDescription());
    }

    @MediumTest
    public void testDeleteDocument() {
        Task task = new Task("task1", new Date());
        taskRepository.add(task);

        taskRepository.remove(task);

        assertFalse(taskRepository.contains(task.getId()));
    }
}
