package com.android.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class Document extends CouchDbDocument {
    @JsonProperty
    private Set<String> forGroups = new HashSet<String>();

    protected void shareWith(String... groupNames) {
        this.forGroups = new HashSet<String>(asList(groupNames));
    }
}
