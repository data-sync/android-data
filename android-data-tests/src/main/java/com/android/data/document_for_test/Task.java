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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!createdDate.equals(task.createdDate)) return false;
        if (!description.equals(task.description)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = createdDate.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "createdDate=" + createdDate +
                ", description='" + description + '\'' +
                '}';
    }
}
