package com.android.data;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class InstrumentationTest extends AndroidTestCase {
    @SmallTest
    public void testSomething() {
        assertEquals("1", "1");
    }
}
