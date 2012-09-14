package org.musicbrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class CollectionListLoader extends AsyncTaskLoader<AsyncResult<List<EditorCollectionStub>>> {

    private MusicBrainz client;

    public CollectionListLoader() {
        super(App.getContext());
        client = App.getWebClient();
    }
    
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public AsyncResult<List<EditorCollectionStub>> loadInBackground() {
        try {
            return new AsyncResult<List<EditorCollectionStub>>(LoaderStatus.SUCCESS, client.lookupUserCollections());
        } catch (IOException e) {
            return new AsyncResult<List<EditorCollectionStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

}
