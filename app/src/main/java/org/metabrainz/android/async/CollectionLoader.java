package org.metabrainz.android.async;

import java.io.IOException;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.api.data.UserCollection;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;

public class CollectionLoader extends PersistingAsyncTaskLoader<AsyncResult<UserCollection>> {

    private MusicBrainz client;
    private String mbid;
    
    public CollectionLoader(String mbid) {
        client = App.getWebClient();
        this.mbid = mbid;
    }
    
    @Override
    public AsyncResult<UserCollection> loadInBackground() {
        try {
            data = new AsyncResult<UserCollection>(LoaderStatus.SUCCESS, client.lookupCollection(mbid));
            return data;
        } catch (IOException e) {
            return new AsyncResult<UserCollection>(LoaderStatus.EXCEPTION, e);
        }
    }

}
