package com.android.data.notification;

import com.android.data.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class NotificationDocument extends Document<NotificationDocument> {
    private String title;

    private String text;

    @JsonCreator
    public NotificationDocument(@JsonProperty("title") String title,
                                @JsonProperty("text") String text) {
        super(NotificationDocument.class);
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
