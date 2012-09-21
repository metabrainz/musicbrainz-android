package org.musicbrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.UserCollectionInfo;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class CollectionListLoader extends AsyncTaskLoader<AsyncResult<List<UserCollectionInfo>>> {

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
    public AsyncResult<List<UserCollectionInfo>> loadInBackground() {
        try {
            return new AsyncResult<List<UserCollectionInfo>>(LoaderStatus.SUCCESS, client.lookupUserCollections());
        } catch (IOException e) {
            return new AsyncResult<List<UserCollectionInfo>>(LoaderStatus.EXCEPTION, e);
        }
    }

}
