package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ObserversTest extends BaseTestCase {
    @Mock
    private ContentObserver<Task> contentObserver;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initMocks(this);
    }

    @MediumTest
    public void testFollowChangesInDocument() throws InterruptedException {
        Task task = new Task("Desc1");
        taskRepository.add(task);
        taskRepository.add(new Task("Another Doc"));

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        taskRepository.registerDocumentObserver(task, contentObserver);

        task.updateDescription("Desc2");
        taskRepository.update(task);

        sleepEnoughForChangesToBeFollowed();
        verify(contentObserver).onChange(taskCaptor.capture());
        assertEquals("Desc2", taskCaptor.getValue().getDescription());
    }
}
