package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;

import java.util.List;

public class SyncTest extends BaseTestCase {
    @MediumTest
    public void replicationWIP() throws InterruptedException {
        Task task = new Task("Please Replicate!!");
        taskRepository.add(task);
        List<Task> allTasks = taskRepository.getAll();
        int size = allTasks.size();
    }
}
