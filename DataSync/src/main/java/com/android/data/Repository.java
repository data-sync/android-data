package com.android.data;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.android.data.tasks.DataChangesFeedAsyncTask;
import com.couchbase.cblite.CBLDatabase;
import com.couchbase.cblite.CBLFilterBlock;
import com.couchbase.cblite.CBLRevision;
import com.couchbase.cblite.CBLView;
import com.couchbase.cblite.CBLViewMapBlock;
import com.couchbase.cblite.CBLViewMapEmitBlock;
import com.couchbase.cblite.CBLViewReduceBlock;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.android.util.ChangesFeedAsyncTask;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.DocumentChange;
import org.ektorp.support.CouchDbRepositorySupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.data.DataHelper.byTypeName;
import static com.android.data.DataHelper.typeName;
import static java.lang.String.format;
import static org.ektorp.impl.NameConventions.capitalize;

public class Repository<T extends Document> extends CouchDbRepositorySupport<T> {
    private final CBLDatabase database;
    private Map<ContentObserver<T>, ChangesFeedAsyncTask> observers = new HashMap<ContentObserver<T>, ChangesFeedAsyncTask>();

    public Repository(Class<T> type, DataStore dataStore) {
        super(type, dataStore.getConnector());
        database = dataStore.getDatabase();
        installTypeFilters();
    }

    public void defineView(String viewName, CBLViewMapBlock mapBlock, CBLViewReduceBlock reduceBlock, String version) {
        CBLView view = database.getViewNamed(format("%s/%s", typeName(type), viewName));
        view.setMapReduceBlocks(mapBlock, reduceBlock, version);
    }

    public void defineView(String viewName, CBLViewMapBlock mapBlock, String version) {
        defineView(viewName, mapBlock, null, version);
    }

    public void defineView(String viewName, CBLViewMapBlock mapBlock) {
        defineView(viewName, mapBlock, "1.0");
    }

    public void defineViewBy(final String fieldName) {
        CBLViewMapBlock mapBlock = new CBLViewMapBlock() {
            @Override
            public void map(Map<String, Object> document, CBLViewMapEmitBlock emitter) {
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void registerContentObserver(final ChangesCommand command, final ContentObserver<T> observer) {
        final DataChangesFeedAsyncTask changesFeedAsyncTask = new DataChangesFeedAsyncTask(db, command, true) {
            @Override
            protected void handleDocumentChange(DocumentChange change) {
                if(change.isDeleted()){
                    observer.onDelete(change.getId(), change.getRevision());
                }else{
                    observer.onChange(DataHelper.fromJson(change.getDocAsNode(), type));
                }
            }
        };
        changesFeedAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        observers.put(observer, changesFeedAsyncTask);
    }

    public void registerDocumentObserver(T document, final ContentObserver<T> observer) {
        final String docId = document.getId();
//        T documentWithSequence = get(docId, new Options().param("local_seq", "true"));
        final ChangesCommand command = observer.commandToFollowChangesContinuously();

        registerContentObserver(command, new ContentObserver<T>() {
            @Override
            public void onChange(T changedDocument) {
                if(docId.equals(changedDocument.getId())) {
                    observer.onChange(changedDocument);
                }
            }

            @Override
            public void onDelete(String docId, String revision) {
                observer.onDelete(docId, revision);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
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

    private void installTypeFilters() {
        database.defineFilter(byTypeName(type), new CBLFilterBlock() {
            @Override
            public boolean filter(CBLRevision revision) {
                return revision.isDeleted() || typeName(type).equals(revision.getProperties().get("type"));
            }
        });
    }
}