package com.android.data;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class PersistenceTest extends InstrumentationTestCase {
    private static class DomainClass extends Document {
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

    @MediumTest
    public void testSaveData() {
        Repository<DomainClass> repository = new Repository<DomainClass>(DomainClass.class);
        DomainClass data = new DomainClass(1, 2);
        repository.add(data);

        DomainClass actualData = repository.get(data.getId());
        assertEquals(data.getAttr1(), actualData.getAttr1());
    }
}
