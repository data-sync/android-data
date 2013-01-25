package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;
import junit.framework.Assert;
import org.ektorp.changes.ChangesCommand;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ObserversTest extends BaseTestCase {
    @Mock
    private ContentObserver<Task> taskObserver;
    private Task task;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initMocks(this);
        ChangesCommand changesCommand = taskChangesCommand();
        task = new Task("Desc1");
        given(taskObserver.commandToFollowChangesContinuously()).willReturn(changesCommand);
        taskRepository.add(task);

    }

    @MediumTest
    public void testFollowChangesInDocument() throws InterruptedException {
        taskRepository.registerDocumentObserver(task, taskObserver);

        task.updateDescription("Desc2");
        taskRepository.update(task);

        sleepEnoughForChangesToBeFollowed();
        verify(taskObserver).onChange(task);
    }

    @MediumTest
    public void testFollowDeletes() throws InterruptedException {
        taskRepository.registerDocumentObserver(task, taskObserver);
        taskRepository.remove(task);

        sleepEnoughForChangesToBeFollowed();
        verify(taskObserver).onDelete(eq(task.getId()), anyString());
    }

    private ChangesCommand taskChangesCommand() {
        return new ContentObserver<Task>(){
            @Override
            public void onChange(Task document) {
                Assert.fail("Im a just a changes command generator. You should never see me!");
            }
        }.commandToFollowChangesContinuously();
    }
}
