package com.android.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.android.util.CouchbaseViewListAdapter;

import java.io.IOException;

public abstract class DataListAdapter<T extends Document> extends CouchbaseViewListAdapter {
    private final int layout;
    private Class<T> type;

    public DataListAdapter(Repository<T> repository, ViewQuery viewQuery, int layout, boolean followChanges, Class<T> type) {
        super(repository.getConnector(), viewQuery, followChanges);
        this.layout = layout;
        this.type = type; // TODO: Can TypeReference be used?
    }

    public DataListAdapter(Repository<T> repository, String viewName, int layout, boolean followChanges, Class<T> type) {
        this(repository, repository.buildViewQuery(viewName), layout, followChanges, type);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(parent);
        } else {
            view = convertView;
        }
        populateView(view, getDocument(position));
        return view;
    }

    public T getDocument(int position) {
        try {
            ViewResult.Row row = getRow(position);
            return (new ObjectMapper()).readValue(row.getValueAsNode(), type);
        } catch (IOException e) {
            Log.e(getClass().getName(), "Not able to de-serialize");
            return null;
        }
    }

    private View newView(ViewGroup parent) {
        LayoutInflater inflater = getInflater(parent);
        return inflater.inflate(layout, parent, false);
    }

    private LayoutInflater getInflater(ViewGroup parent) {
        return (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    abstract View populateView(View view, T document);
}