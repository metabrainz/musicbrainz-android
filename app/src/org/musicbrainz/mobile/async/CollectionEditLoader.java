package org.musicbrainz.mobile.async;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class CollectionEditLoader extends AsyncTaskLoader<AsyncResult<Void>> {
    
    private MusicBrainz client;
    private String collectionMbid;
    private String releaseMbid;
    private boolean isAdd;
    
    public CollectionEditLoader(String collectionMbid, String releaseMbid, boolean isAdd) {
        super(App.getContext());
        client = App.getWebClient();
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
