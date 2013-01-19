package com.android.data;

import android.os.AsyncTask;
import android.util.Log;
import com.couchbase.touchdb.*;
import org.ektorp.*;
import org.ektorp.android.util.ChangesFeedAsyncTask;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.DocumentChange;
import org.ektorp.support.CouchDbRepositorySupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.data.DataJsonHelper.fromJson;
import static java.lang.String.format;
import static org.ektorp.impl.NameConventions.capitalize;

public class Repository<T extends Document> extends CouchDbRepositorySupport<T> {
    private final TDDatabase database;
    private Map<ContentObserver<T>, ChangesFeedAsyncTask> observers = new HashMap<ContentObserver<T>, ChangesFeedAsyncTask>();

    public Repository(Class<T> type, DataStore dataStore) {
        super(type, dataStore.getConnector());
        database = dataStore.getDatabase();
        installTypeFilters();
    }

    public void defineView(String viewName, TDViewMapBlock mapBlock, TDViewReduceBlock reduceBlock, String version) {
        TDView view = database.getViewNamed(format("%s/%s", typeName(), viewName));
        view.setMapReduceBlocks(mapBlock, reduceBlock, version);
    }

    public void defineView(String viewName, TDViewMapBlock mapBlock, String version) {
        defineView(viewName, mapBlock, null, version);
    }

    public void defineView(String viewName, TDViewMapBlock mapBlock) {
        defineView(viewName, mapBlock, "1.0");
    }

    public void defineViewBy(final String fieldName) {
        TDViewMapBlock mapBlock = new TDViewMapBlock() {
            @Override
            public void map(Map<String, Object> document, TDViewMapEmitBlock emitter) {
                Object value = document.get(fieldName);
                if (value != null) {
                    emitter.emit(value.toString(), document);
                }
            }
        };

        defineView("by".concat(capitalize(fieldName)), mapBlock);
    }

    public List<T> query(String viewName) {
        return queryView(viewName);
    }

    public List<T> query(String viewName, Object key) {
        return query(buildViewQuery(viewName).key(key));
    }

    public List<T> query(String viewName, ComplexKey key) {
        return queryView(viewName, key);
    }

    public List<T> query(ViewQuery viewQuery) {
        Log.d(getClass().getName(), viewQuery.buildQuery());
        return db.queryView(viewQuery, type);
    }

    public ViewQuery buildViewQuery(String viewName) {
        return createQuery(viewName).includeDocs(true);
    }

    public void registerDocumentObserver(T document, final ContentObserver<T> observer) {
        final String docId = document.getId();
        T documentWithSequence = get(docId, new Options().param("local_seq", "true"));
        final ChangesCommand command = new ChangesCommand.Builder()
                .continuous(true)
                .since(documentWithSequence.getSequence())
                .filter(byTypeName())
                .heartbeat(5000)
                .includeDocs(true)
                .build();

        final ChangesFeedAsyncTask changesFeedAsyncTask = new ChangesFeedAsyncTask(db, command) {
            @Override
            protected void handleDocumentChange(DocumentChange change) {
                if(docId.equals(change.getId())) {
                    observer.onChange(fromJson(change.getDoc(), type));
                }
            }
        };
        changesFeedAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        observers.put(observer, changesFeedAsyncTask);
    }

    public void unregisterContentObserver(final ContentObserver<T> observer) {
        observers.get(observer).cancel(false);
        observers.remove(observer);
    }

    public void reset() {
        database.deleteDatabase();
        db.createDatabaseIfNotExists();
    }

    protected CouchDbConnector getConnector() {
        return db;
    }

    private String byTypeName() {
        return "by" + typeName();
    }

    private String typeName() {
        return type.getSimpleName();
    }

    private void installTypeFilters() {
        database.defineFilter(byTypeName(), new TDFilterBlock() {
            @Override
            public boolean filter(TDRevision revision) {
                return typeName().equals(revision.getProperties().get("type"));
            }
        });
    }
}