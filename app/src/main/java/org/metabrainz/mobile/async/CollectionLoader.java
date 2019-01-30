package org.metabrainz.mobile.async;

import java.io.IOException;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.api.data.UserCollection;
import org.metabrainz.mobile.App;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.async.result.LoaderStatus;

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
