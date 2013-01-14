package com.android.data;

import android.util.Log;
import com.couchbase.touchdb.*;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.ektorp.impl.NameConventions.capitalize;

public class Repository<T extends Document> extends CouchDbRepositorySupport<T> {
    private final TDDatabase database;

    public Repository(Class<T> type, DataStore dataStore) {
        super(type, dataStore.getConnector());
        database = dataStore.getDatabase();
    }

    public void defineView(String viewName, TDViewMapBlock mapBlock, TDViewReduceBlock reduceBlock, String version) {
        TDView view = database.getViewNamed(format("%s/%s", type.getSimpleName(), viewName));
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

    public void reset() {
        database.deleteDatabase();
        db.createDatabaseIfNotExists();
    }

    public CouchDbConnector getConnector() {
        return db;
    }
}