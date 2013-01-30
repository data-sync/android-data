package com.android.data.notification;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import com.android.data.ContentObserver;
import com.android.data.DataStore;
import com.android.data.Repository;
import org.ektorp.changes.ChangesCommand;

public class NotificationObserver extends ContentObserver<NotificationDocument> {
    private final Context context;

    public NotificationObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onChange(NotificationDocument document) {
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_popup_reminder)
                .setContentTitle(document.getTitle())
                .setContentText(document.getText())
                .getNotification();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }

    public ChangesCommand changesCommandToFollow() {
        return commandToFollowChangesContinuously();
    }

    public static Repository<NotificationDocument> setup(DataStore dataStore) {
        NotificationObserver notificationObserver = new NotificationObserver(dataStore.getContext());
        return setup(dataStore, notificationObserver);
    }

    public static Repository<NotificationDocument> setup(DataStore dataStore, NotificationObserver notificationObserver) {
        Repository<NotificationDocument> notificationRepo = new Repository<NotificationDocument>(NotificationDocument.class, dataStore);
        notificationRepo.registerContentObserver(notificationObserver.changesCommandToFollow(), notificationObserver);
        return notificationRepo;
    }
}
