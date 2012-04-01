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

import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ReleaseActivity;
import org.musicbrainz.mobile.adapter.list.ReleaseStubAdapter;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;
import org.musicbrainz.mobile.loader.CollectionLoader;
import org.musicbrainz.mobile.loader.result.AsyncResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class CollectionFragment extends ListFragment implements LoaderCallbacks<AsyncResult<EditorCollection>>{
    
    private static final int COLLECTION_LOADER = 0;
    private Context appContext;
    private String mbid;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContext = activity.getApplicationContext();
        mbid = activity.getIntent().getStringExtra(Extra.COLLECTION_MBID);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COLLECTION_LOADER, savedInstanceState, this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public Loader<AsyncResult<EditorCollection>> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(appContext, mbid);
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<EditorCollection>> loader, AsyncResult<EditorCollection> data) {
        handleResult(data);
    }

    private void handleResult(AsyncResult<EditorCollection> result) {
        switch (result.getStatus()) {
        case SUCCESS:
            EditorCollection collection = result.getData();
            setListAdapter(new ReleaseStubAdapter(getActivity(), collection.getReleases()));
            break;
        case EXCEPTION:
            showConnectionErrorWarning();
        }
    }

    private void showConnectionErrorWarning() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<EditorCollection>> loader) {
        loader.reset();
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ReleaseStubAdapter adapter = (ReleaseStubAdapter) getListAdapter();
        String releaseMbid = adapter.getItem(position).getReleaseMbid();
        Intent intent = new Intent(appContext, ReleaseActivity.class);
        intent.putExtra(Extra.RELEASE_MBID, releaseMbid);
        startActivity(intent);
    }

}
