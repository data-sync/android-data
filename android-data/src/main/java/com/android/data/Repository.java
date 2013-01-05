package com.android.data;

import com.couchbase.touchdb.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;

import java.io.IOException;
import java.util.ArrayList;
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
        TDView view = database.getViewNamed(format("%s/%s", DESIGN_DOCS, viewName));
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

    public List<T> byView(String viewName) throws IOException {
        ViewQuery viewQuery = new ViewQuery().designDocId(format("%s/%s", "_design", DESIGN_DOCS)).viewName(viewName).descending(true);
        //TODO: Why db.queryView(viewQuery, type) is not working??
        ViewResult result = db.queryView(viewQuery);
        List<ViewResult.Row> rows = result.getRows();
        List<T> objects = new ArrayList<T>();
        for (ViewResult.Row row : rows) {
            T object = (new ObjectMapper()).readValue(row.getValueAsNode(), type);
            objects.add(object);
        }
        return objects;
    }

    public TDDatabase getDatabase() {
        return database;
    }

    public void reset() {
        database.deleteDatabase();
        db.createDatabaseIfNotExists();
    }
}