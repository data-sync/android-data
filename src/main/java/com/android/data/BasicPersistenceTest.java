package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;

public class BasicPersistenceTest extends BaseTestCase {
    @MediumTest
    public void testSaveDocument() {
        TestDocument data = new TestDocument(1, 2);
        repository.add(data);

        TestDocument actualData = repository.get(data.getId());
        assertEquals(data.getAttr1(), actualData.getAttr1());
        assertEquals(data.getAttr2(), actualData.getAttr2());
    }

    @MediumTest
    public void testUpdateDocument() {
        TestDocument data = new TestDocument(1, 2);
        repository.add(data);

        data.setAttr1(3);
        repository.update(data);

        TestDocument actualData = repository.get(data.getId());
        assertEquals(3, actualData.getAttr1());
    }

    @MediumTest
    public void testDeleteDocument() {
        TestDocument data = new TestDocument(1, 2);
        repository.add(data);

        repository.remove(data);

        assertFalse(repository.contains(data.getId()));
    }
}
