package com.android.data;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.data.tasks.DataChangesFeedAsyncTask;
import org.ektorp.CouchDbConnector;
import org.ektorp.DbAccessException;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.android.util.CouchbaseViewListAdapter;
import org.ektorp.android.util.EktorpAsyncTask;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.DocumentChange;

import static com.android.data.DataHelper.*;

public abstract class DataListAdapter<T extends Document> extends CouchbaseViewListAdapter {
    private final int layout;

    public DataListAdapter(Repository<T> repository, ViewQuery viewQuery, int layout, boolean followChanges) {
        super(repository.getConnector(), viewQuery, followChanges);
        this.layout = layout;
    }

    public DataListAdapter(Repository<T> repository, String viewName, int layout, boolean followChanges) {
        this(repository, repository.buildViewQuery(viewName), layout, followChanges);
    }

    @Override
    public ViewResult.Row getRow(int position) {
        return listRows.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(parent);
        } else {
            view = convertView;
        }
        populateView(view, getItem(position), position);
        return view;
    }

    @Override
    public T getItem(int position) {
        return fromJson(getRow(position).getValueAsNode(), DataHelper.<T>getGenericClass(this.getClass()));
    }

    private View newView(ViewGroup parent) {
        LayoutInflater inflater = getInflater(parent);
        return inflater.inflate(layout, parent, false);
    }

    private LayoutInflater getInflater(ViewGroup parent) {
        return (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Fix to run the changes watcher in ThreadPool, so that they are alive - START

    protected ListChangesAsyncTask couchChangesAsyncTask;

    @Override
    protected void updateListItems() {
        if (updateListItemsTask == null) {

            updateListItemsTask = new EktorpAsyncTask() {

                protected ViewResult viewResult;

                @Override
                protected void doInBackground() {
                    viewResult = couchDbConnector.queryView(viewQuery);
                }

                protected void onSuccess() {
                    if (viewResult != null) {
                        lastUpdateView = viewResult.getUpdateSeq();
                        listRows = viewResult.getRows();
                        notifyDataSetChanged();
                    }
                    updateListItemsTask = null;

                    if (couchChangesAsyncTask == null && followChanges) {
                        ChangesCommand changesCmd = new ChangesCommand.Builder().since(lastUpdateView)
                                .includeDocs(false)
                                .continuous(true)
                                .filter(byTypeName(getGenericClass(DataListAdapter.this.getClass())))
                                .heartbeat(5000)
                                .build();

                        couchChangesAsyncTask = new ListChangesAsyncTask(couchDbConnector, changesCmd);
                        couchChangesAsyncTask.execute();
                    }

                    if (lastUpdateChangesFeed > lastUpdateView) {
                        updateListItems();
                    }

                }

                @Override
                protected void onDbAccessException(
                        DbAccessException dbAccessException) {
                    handleViewAsyncTaskDbAccessException(dbAccessException);
                }

            };

            updateListItemsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class ListChangesAsyncTask extends DataChangesFeedAsyncTask {

        public ListChangesAsyncTask(CouchDbConnector couchDbConnector,
                                    ChangesCommand changesCommand) {
            super(couchDbConnector, changesCommand, true);
        }

        @Override
        protected void handleDocumentChange(DocumentChange change) {
            lastUpdateChangesFeed = change.getSequence();
            updateListItems();
        }
    }

    // END

    abstract public void populateView(View view, T document, int position);
}