package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

public class CollectionLoader extends PersistingAsyncTaskLoader<AsyncResult<EditorCollection>> {

    private MusicBrainz client;
    private String mbid;
    
    public CollectionLoader(String mbid) {
        client = App.getWebClient();
        this.mbid = mbid;
    }
    
    @Override
    public AsyncResult<EditorCollection> loadInBackground() {
        try {
            data = new AsyncResult<EditorCollection>(LoaderStatus.SUCCESS, client.lookupCollection(mbid));
            return data;
        } catch (IOException e) {
            return new AsyncResult<EditorCollection>(LoaderStatus.EXCEPTION, e);
        }
    }

}
