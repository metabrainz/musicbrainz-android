/*
 * Copyright (C) 2012 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.fragment;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.CollectionListAdapter;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.loader.CollectionListLoader;
import org.musicbrainz.mobile.loader.result.AsyncResult;

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
        LoaderCallbacks<AsyncResult<LinkedList<EditorCollectionStub>>> {

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
    public Loader<AsyncResult<LinkedList<EditorCollectionStub>>> onCreateLoader(int id, Bundle args) {
        return new CollectionListLoader(appContext);
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<LinkedList<EditorCollectionStub>>> loader,
            AsyncResult<LinkedList<EditorCollectionStub>> data) {
        hideLoading();
        handleResult(data);
    }

    private void handleResult(AsyncResult<LinkedList<EditorCollectionStub>> result) {
        switch (result.getStatus()) {
        case SUCCESS:
            LinkedList<EditorCollectionStub> collection = result.getData();
            try {
                setListAdapter(new CollectionListAdapter(getActivity(), collection));
            } catch (Exception e) {
                showConnectionErrorWarning(); // Fragment not connected.
            }
            break;
        case EXCEPTION:
            showConnectionErrorWarning();
        }
    }
    
    private void hideLoading() {
        loading.setVisibility(View.GONE);
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
    public void onLoaderReset(Loader<AsyncResult<LinkedList<EditorCollectionStub>>> loader) {
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
