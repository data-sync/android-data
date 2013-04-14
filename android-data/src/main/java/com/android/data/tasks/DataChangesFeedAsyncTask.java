package com.android.data.tasks;

import android.util.Log;
import org.ektorp.CouchDbConnector;
import org.ektorp.DbAccessException;
import org.ektorp.android.util.ChangesFeedAsyncTask;
import org.ektorp.changes.ChangesCommand;

import java.util.Arrays;

abstract public class DataChangesFeedAsyncTask extends ChangesFeedAsyncTask {
    private final boolean ignoreDBAccessException;

    public DataChangesFeedAsyncTask(CouchDbConnector couchDbConnector, ChangesCommand changesCommand, boolean ignoreDBAccessException) {
        super(couchDbConnector, changesCommand);
        this.ignoreDBAccessException = ignoreDBAccessException;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            return super.doInBackground(params);
        } catch (DbAccessException exception) {
            return exception;
        }
    }

    @Override
    protected void onDbAccessException(DbAccessException dbAccessException) {
        if (ignoreDBAccessException) {
            Log.d(getClass().getName(), "DB Exception while following changes feed, but ignored:\n " + Arrays.toString(dbAccessException.getStackTrace()));
        } else {
            super.onDbAccessException(dbAccessException);
        }
    }
}
