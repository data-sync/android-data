package com.android.data.document_for_test;

import com.android.data.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class Task extends Document {
    private Date createdDate;

    private String description;

    @JsonCreator
    public Task(@JsonProperty("description") String description,
                @JsonProperty("createdDate") Date createdDate) {
        this.description = description;
        this.createdDate = createdDate;
    }

    public Task(String description) {
        this(description, new Date());
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
