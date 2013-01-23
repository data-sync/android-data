package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ObserversTest extends BaseTestCase {
    @Mock
    private ContentObserver<Task> contentObserver;
    @Captor
    private ArgumentCaptor<Task> taskCaptor;

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

        taskRepository.registerDocumentObserver(task, contentObserver);

        task.updateDescription("Desc2");
        taskRepository.update(task);

        sleepEnoughForChangesToBeFollowed();
        verify(contentObserver).onChange(taskCaptor.capture());
        assertEquals("Desc2", taskCaptor.getValue().getDescription());
    }

    @MediumTest
    public void testFollowDeletes() throws InterruptedException {
        Task task = new Task("Desc1");
        taskRepository.add(task);

        taskRepository.registerDocumentObserver(task, contentObserver);
        taskRepository.remove(task);

        sleepEnoughForChangesToBeFollowed();
        verify(contentObserver).onDelete(eq(task.getId()), anyString());
    }
}
