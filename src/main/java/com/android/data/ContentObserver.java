package com.android.data;

import org.ektorp.changes.ChangesCommand;

import static com.android.data.DataHelper.byTypeName;
import static com.android.data.DataHelper.getGenericClass;

abstract public class ContentObserver<T extends Document> {
    abstract public void onChange(T document);

    public void onDelete(String docId, String revision){
    }

    protected ChangesCommand commandToFollowChangesContinuously() {
        return new ChangesCommand.Builder()
                .continuous(true)
                .filter(byTypeName(getGenericClass(getClass())))
                .heartbeat(5000)
                .includeDocs(true)
                .build();
    }
}
