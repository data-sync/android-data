package com.android.data;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class PersistenceTest extends InstrumentationTestCase {

    private Repository<DomainClass> repository;

    private static class DomainClass extends Document {
        DomainClass() {}
        private int attr1;
        private int attr2;

        private DomainClass(int attr1, int attr2) {
            this.attr1 = attr1;
            this.attr2 = attr2;
        }

        public int getAttr1() {
            return attr1;
        }

        public int getAttr2() {
            return attr2;
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        repository = new Repository<DomainClass>(DomainClass.class, new DataStore(getContext(), "test_db"));
    }

    @MediumTest
    public void testSaveData() {
        DomainClass data = new DomainClass(1, 2);
        repository.add(data);

        DomainClass actualData = repository.get(data.getId());
        assertEquals(data.getAttr1(), actualData.getAttr1());
        assertEquals(data.getAttr2(), actualData.getAttr2());
    }

    private Context getContext() {
        return getInstrumentation().getContext();
    }
}
