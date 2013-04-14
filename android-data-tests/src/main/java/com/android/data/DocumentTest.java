package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.models.Task;

public class DocumentTest extends BaseTestCase {

    @MediumTest
    public void testSetTypeName() {
        Task task = new Task("Desc");
        assertEquals("Task", task.getType());
    }

}
