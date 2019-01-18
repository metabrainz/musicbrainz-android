package org.metabrainz.android.async;

import java.io.IOException;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.api.webservice.Entity;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

public class SubmitRatingLoader extends AsyncTaskLoader<AsyncResult<Float>> {

    private MusicBrainz client;
    private Entity type;
    private String mbid;
    private int rating;

    public SubmitRatingLoader(Entity type, String mbid, int rating) {
        super(App.getContext());
        client = App.getWebClient();
        this.type = type;
        this.mbid = mbid;
        this.rating = rating;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<Float> loadInBackground() {
        try {
            client.submitRating(type, mbid, rating);
            Float updatedRating = client.lookupRating(type, mbid);
            return new AsyncResult<Float>(LoaderStatus.SUCCESS, updatedRating);
        } catch (IOException e) {
            return new AsyncResult<Float>(LoaderStatus.EXCEPTION, e);
        }
    }
}
