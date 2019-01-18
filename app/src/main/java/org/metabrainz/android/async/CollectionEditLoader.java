package org.metabrainz.android.async;

import java.io.IOException;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

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
