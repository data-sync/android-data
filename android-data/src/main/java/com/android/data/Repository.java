package com.android.data;

import com.couchbase.touchdb.*;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.ektorp.impl.NameConventions.capitalize;

public class Repository<T extends Document> extends CouchDbRepositorySupport<T> {
    public static final String DESIGN_DOCS = "designDocs";
    private final TDDatabase database;

    protected Repository(Class<T> type, DataStore dataStore) {
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

    public List<T> query(String byViewName) throws IOException {
        return queryView(byViewName);
    }

    public List<T> query(ViewQuery viewQuery) throws IOException {
        return db.queryView(viewQuery, type);
    }

    public TDDatabase getDatabase() {
        return database;
    }

    public void reset() {
        database.deleteDatabase();
        db.createDatabaseIfNotExists();
    }
}