package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class CollectionEditLoader extends AsyncTaskLoader<AsyncResult<Void>> {
    
    private String collectionMbid;
    private String releaseMbid;
    private boolean isAdd;
    
    public CollectionEditLoader(String collectionMbid, String releaseMbid, boolean isAdd) {
        super(App.getContext());
        this.collectionMbid = collectionMbid;
        this.releaseMbid = releaseMbid;
        this.isAdd = isAdd;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<Void> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(App.getCredentials());
            if (isAdd) {
                client.addReleaseToCollection(collectionMbid, releaseMbid);
            } else {
                client.deleteReleaseFromCollection(collectionMbid, releaseMbid);
            }
            return new AsyncResult<Void>(LoaderStatus.SUCCESS);
        } catch (IOException e) {
            return new AsyncResult<Void>(LoaderStatus.EXCEPTION, e);
        }
    }

}
