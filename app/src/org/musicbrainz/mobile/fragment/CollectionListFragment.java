package org.musicbrainz.mobile.fragment;

import java.util.List;

import org.musicbrainz.android.api.data.UserCollectionInfo;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.CollectionListAdapter;
import org.musicbrainz.mobile.async.CollectionListLoader;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.intent.IntentFactory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class CollectionListFragment extends ListFragment implements
        LoaderCallbacks<AsyncResult<List<UserCollectionInfo>>> {

    private static final int COLLECTIONS_LOADER = 0;
    private Context appContext;
    private View loading;
    private View error;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContext = activity.getApplicationContext();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COLLECTIONS_LOADER, savedInstanceState, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_collections, container, false);
        loading = layout.findViewById(R.id.loading);
        error = layout.findViewById(R.id.error);
        return layout;
    }

    @Override
    public Loader<AsyncResult<List<UserCollectionInfo>>> onCreateLoader(int id, Bundle args) {
        return new CollectionListLoader();
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<List<UserCollectionInfo>>> loader,
            AsyncResult<List<UserCollectionInfo>> data) {
        loading.setVisibility(View.GONE);
        handleResult(data);
    }

    private void handleResult(AsyncResult<List<UserCollectionInfo>> result) {
        switch (result.getStatus()) {
        case SUCCESS:
            List<UserCollectionInfo> collection = result.getData();
            try {
                setListAdapter(new CollectionListAdapter(getActivity(), collection));
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
                getLoaderManager().restartLoader(COLLECTIONS_LOADER, null, CollectionListFragment.this);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<List<UserCollectionInfo>>> loader) {
        loader.reset();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CollectionListAdapter adapter = (CollectionListAdapter) getListAdapter();
        String title = adapter.getItem(position).getName();
        String mbid = adapter.getItem(position).getMbid();
        startActivity(IntentFactory.getCollection(appContext, title, mbid));
    }

}
