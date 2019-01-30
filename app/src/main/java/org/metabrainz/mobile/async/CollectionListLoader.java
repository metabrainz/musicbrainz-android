package org.metabrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.api.data.UserSearchResult;
import org.metabrainz.mobile.App;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

public class CollectionListLoader extends AsyncTaskLoader<AsyncResult<List<UserSearchResult>>> {

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
    public AsyncResult<List<UserSearchResult>> loadInBackground() {
        try {
            return new AsyncResult<List<UserSearchResult>>(LoaderStatus.SUCCESS, client.lookupUserCollections());
        } catch (IOException e) {
            return new AsyncResult<List<UserSearchResult>>(LoaderStatus.EXCEPTION, e);
        }
    }

}
