package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;
import com.android.data.document_for_test.Task;
import com.couchbase.touchdb.TDViewMapBlock;
import com.couchbase.touchdb.TDViewMapEmitBlock;
import org.ektorp.ComplexKey;
import org.ektorp.ViewQuery;

import java.util.*;

public class FindByViewTest extends BaseTestCase {

    private Task task1;
    private Task task2;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        taskRepository.defineViewBy("description");
        task1 = new Task("Desc1");
        task2 = new Task("Desc2");
        taskRepository.add(task1);
        taskRepository.add(task2);
    }

    @MediumTest
    public void testFindDocByView() {
        List<Task> tasks = taskRepository.query("byDescription");
        assertEquals(2, tasks.size());
        assertEquals(Arrays.asList(task1, task2), tasks);
    }

    @MediumTest
    public void testFindDocsWithKey() {
        List<Task> tasks = taskRepository.query("byDescription", "Desc1");
        assertEquals(1, tasks.size());
        assertEquals(task1, tasks.get(0));
    }

    @MediumTest
    public void testFindDocsWithCustomQuery() {
        ViewQuery query = taskRepository.buildViewQuery("byDescription").key("Desc1");
        List<Task> tasks = taskRepository.query(query);
        assertEquals(1, tasks.size());
        assertEquals(task1, tasks.get(0));
    }

    @MediumTest
    public void testFindDocsWithComplexKeys() {
        Date task3CreatedDate = new Date();
        Task task3 = new Task("Desc3", task3CreatedDate);
        taskRepository.defineView("byDescriptionAndDate", new TDViewMapBlock() {
            @Override
            public void map(Map<String, Object> document, TDViewMapEmitBlock emitter) {
                emitter.emit(new Object[]{document.get("description"), document.get("createdDate")}, document);
            }
        });
        taskRepository.add(task3);

        List<Task> tasks = taskRepository.query("byDescriptionAndDate", ComplexKey.of("Desc3", task3CreatedDate));
        assertEquals(1, tasks.size());
        assertEquals(task3, tasks.get(0));
    }
}
