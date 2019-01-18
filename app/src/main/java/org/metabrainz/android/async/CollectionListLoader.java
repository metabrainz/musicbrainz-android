package org.metabrainz.android.async;

import java.io.IOException;
import java.util.List;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.api.data.UserCollectionInfo;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

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
