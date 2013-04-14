package com.android.data.adapters;

import android.view.View;
import android.widget.TextView;
import com.android.data.DataListAdapter;
import com.android.data.Repository;
import com.android.data.models.Task;

public class TaskListAdapter extends DataListAdapter<Task> {
    public TaskListAdapter(Repository<Task> repository) {
        super(repository, "byDescription", android.R.layout.simple_list_item_1, true);
    }

    @Override
    public void populateView(View view, Task document, int position) {
        ((TextView) view).setText(document.getDescription());
    }
}
