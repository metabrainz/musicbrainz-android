package org.metabrainz.mobile.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.list.CollectionListAdapter;
import org.metabrainz.mobile.api.data.UserSearchResult;
import org.metabrainz.mobile.async.CollectionListLoader;
import org.metabrainz.mobile.async.result.AsyncResult;

import java.util.List;

public class CollectionAddDialog extends DialogFragment implements
        LoaderCallbacks<AsyncResult<List<UserSearchResult>>>, OnItemClickListener {

    public static final String TAG = "collection_add";
    private static final int COLLECTIONS_LOADER = 10;

    private AddToCollectionCallback callback;
    private View loading;
    private View error;
    private View empty;

    private ListView list;
    private CollectionListAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (AddToCollectionCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + AddToCollectionCallback.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.collection_dialog_title);
        View layout = inflater.inflate(R.layout.dialog_add_collection, container, false);
        list = (ListView) layout.findViewById(android.R.id.list);
        list.setOnItemClickListener(this);
        loading = layout.findViewById(R.id.loading);
        error = layout.findViewById(R.id.error);
        empty = layout.findViewById(android.R.id.empty);
        empty.setVisibility(View.GONE);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COLLECTIONS_LOADER, savedInstanceState, this);
    }

    @Override
    public Loader<AsyncResult<List<UserSearchResult>>> onCreateLoader(int id, Bundle args) {
        return new CollectionListLoader();
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<List<UserSearchResult>>> loader,
                               AsyncResult<List<UserSearchResult>> result) {
        loading.setVisibility(View.GONE);
        switch (result.getStatus()) {
            case SUCCESS:
                List<UserSearchResult> collection = result.getData();
                try {
                    adapter = new CollectionListAdapter(getActivity(), collection);
                    if (adapter.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                    }
                    list.setAdapter(adapter);
                } catch (Exception e) {
                    // Fragment not connected.
                }
                break;
            case EXCEPTION:
                showConnectionErrorWarning();
        }
    }

    private void showConnectionErrorWarning() {
        error.setVisibility(View.VISIBLE);
        Button retry = (Button) error.findViewById(R.id.retry_button);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                getLoaderManager().restartLoader(COLLECTIONS_LOADER, null, CollectionAddDialog.this);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<List<UserSearchResult>>> loader) {
        loader.reset();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String collectionMbid = adapter.getItem(position).getMbid();
        callback.addReleaseToCollection(collectionMbid);
        getDialog().dismiss();
    }

    public interface AddToCollectionCallback {
        void addReleaseToCollection(String collectionMbid);
    }

}
