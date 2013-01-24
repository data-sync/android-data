package com.android.data.notification;

import android.R;
import android.app.Notification;
import android.content.Context;
import com.android.data.ContentObserver;
import org.ektorp.changes.ChangesCommand;

public class NotificationObserver extends ContentObserver<NotificationDocument> {
    private final Context context;

    public NotificationObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onChange(NotificationDocument document) {
        new Notification.Builder(context)
                .setSmallIcon(R.drawable.stat_sys_download_done)
                .setContentTitle(document.getTitle())
                .setContentText(document.getText());
    }

    public ChangesCommand changesCommandToFollow() {
        return commandToFollowChangesContinuously();
    }
}
