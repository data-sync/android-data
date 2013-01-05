package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.document_for_test.Task;

import java.io.IOException;
import java.util.List;

public class FindByViewTest extends BaseTestCase {
    @MediumTest
    public void testFindDocByView() throws IOException {
        repository.defineViewBy("description");
        repository.add(new Task("Desc1"));
        repository.add(new Task("Desc2"));

        List<Task> tasks = repository.byView("bydescription");
        assertEquals(2, tasks.size());
    }
}
