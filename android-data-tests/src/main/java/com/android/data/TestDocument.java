package com.android.data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

class TestDocument extends Document {
    private int attr1;
    private int attr2;

    @JsonCreator
    public TestDocument(@JsonProperty("attr1") int attr1,
                        @JsonProperty("attr2") int attr2) {
        this.attr1 = attr1;
        this.attr2 = attr2;
    }

    public int getAttr1() {
        return attr1;
    }

    public int getAttr2() {
        return attr2;
    }

    public void setAttr1(int attr1) {
        this.attr1 = attr1;
    }
}
