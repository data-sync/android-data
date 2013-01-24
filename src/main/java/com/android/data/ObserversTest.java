package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;

import static org.mockito.MockitoAnnotations.initMocks;

public class ObserversTest extends BaseTestCase {
    private boolean onChangeCalled;
    private boolean documentDeleted;
    private Task documentChanged;

    ContentObserver<Task> taskObserver = new ContentObserver<Task>(){
        @Override
        public void onChange(Task document) {
            documentChanged = document;
            onChangeCalled = true;
        }
        @Override
        public void onDelete(String docId, String revision) {
            documentDeleted = true;
        }
    };

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initMocks(this);
        onChangeCalled = false;
        documentDeleted = false;
    }

    @MediumTest
    public void testFollowChangesInDocument() throws InterruptedException {
        Task task = new Task("Desc1");
        taskRepository.add(task);
        taskRepository.add(new Task("Another Doc"));

        taskRepository.registerDocumentObserver(task, taskObserver);

        task.updateDescription("Desc2");
        taskRepository.update(task);

        sleepEnoughForChangesToBeFollowed();
        assertTrue(onChangeCalled);
        assertEquals("Desc2", documentChanged.getDescription());
    }

    @MediumTest
    public void testFollowDeletes() throws InterruptedException {
        Task task = new Task("Desc1");
        taskRepository.add(task);

        taskRepository.registerDocumentObserver(task, taskObserver);
        taskRepository.remove(task);

        sleepEnoughForChangesToBeFollowed();
        assertTrue(documentDeleted);
    }
}
