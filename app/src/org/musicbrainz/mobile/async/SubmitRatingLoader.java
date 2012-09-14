package org.musicbrainz.mobile.async;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

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
