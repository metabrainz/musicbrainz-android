package org.musicbrainz.mobile.dialog;

import java.util.List;

import org.musicbrainz.android.api.data.UserCollectionInfo;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.CollectionListAdapter;
import org.musicbrainz.mobile.async.CollectionListLoader;
import org.musicbrainz.mobile.async.result.AsyncResult;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class CollectionAddDialog extends DialogFragment implements
        LoaderCallbacks<AsyncResult<List<UserCollectionInfo>>>, OnItemClickListener {

    public static final String TAG = "collection_add";
    private static final int COLLECTIONS_LOADER = 10;
    
    private AddToCollectionCallback callback;
    private View loading;
    private View error;
    private View empty;
    
    private ListView list;
    private CollectionListAdapter adapter;
    
    public interface AddToCollectionCallback {
        void addReleaseToCollection(String collectionMbid);
    }
    
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
    public Loader<AsyncResult<List<UserCollectionInfo>>> onCreateLoader(int id, Bundle args) {
        return new CollectionListLoader();
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<List<UserCollectionInfo>>> loader,
            AsyncResult<List<UserCollectionInfo>> result) {
        loading.setVisibility(View.GONE);
        switch (result.getStatus()) {
        case SUCCESS:
            List<UserCollectionInfo> collection = result.getData();
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
        Button retry = ( Button)error.findViewById(R.id.retry_button);
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
    public void onLoaderReset(Loader<AsyncResult<List<UserCollectionInfo>>> loader) {
        loader.reset();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String collectionMbid = adapter.getItem(position).getMbid();
        callback.addReleaseToCollection(collectionMbid);
        getDialog().dismiss();
    }

}
