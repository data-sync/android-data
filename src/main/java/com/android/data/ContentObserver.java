package com.android.data;

import org.ektorp.changes.ChangesCommand;

import static com.android.data.DataHelper.byTypeName;

abstract public class ContentObserver<T extends Document> {
    abstract public void onChange(T document);

    protected ChangesCommand commandToFollowChangesContinuouslyOn(Class<T> cls) {
        return new ChangesCommand.Builder()
                .continuous(true)
                .filter(byTypeName(cls))
                .heartbeat(5000)
                .includeDocs(true)
                .build();
    }
}
