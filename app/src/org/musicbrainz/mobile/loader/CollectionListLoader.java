package org.musicbrainz.mobile.loader;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class CollectionListLoader extends AsyncTaskLoader<AsyncResult<List<EditorCollectionStub>>> {

    public CollectionListLoader() {
        super(App.getContext());
    }
    
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public AsyncResult<List<EditorCollectionStub>> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(App.getCredentials());
            return new AsyncResult<List<EditorCollectionStub>>(LoaderStatus.SUCCESS, client.lookupUserCollections());
        } catch (IOException e) {
            return new AsyncResult<List<EditorCollectionStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

}
