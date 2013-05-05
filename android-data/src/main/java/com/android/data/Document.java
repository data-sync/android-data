package com.android.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

import java.util.HashSet;
import java.util.Set;

import static com.android.data.DataHelper.getGenericClass;
import static com.android.data.DataHelper.typeName;
import static java.util.Arrays.asList;

abstract public class Document<T extends Document> extends CouchDbDocument {
    @SuppressWarnings("unused")
    @JsonProperty
    private Set<String> forGroups = new HashSet<String>();

    @JsonProperty("_local_seq")
    private String sequence;
    @JsonProperty("type")
    private String type;

    protected Document() {
        Class<T> klass = getGenericClass(this.getClass());
        setType(typeName(klass));
    }

    public String getSequence() {
        return sequence;
    }

    protected void shareWith(String... groupNames) {
        this.forGroups = new HashSet<String>(asList(groupNames));
    }

    private void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}