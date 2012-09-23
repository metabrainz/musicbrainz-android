package org.musicbrainz.mobile.async.lastfm;

import java.io.IOException;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.lastfm.Response.LastFmArtist;
import org.musicbrainz.mobile.util.Log;

import android.support.v4.content.AsyncTaskLoader;

public class ArtistBioLoader extends AsyncTaskLoader<LastFmArtist> {

    private String mbid;

    public ArtistBioLoader(String mbid) {
        super(App.getContext());
        this.mbid = mbid;
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public LastFmArtist loadInBackground() {
        try {
            return new LastFmClient().getArtistInfo(mbid);
        } catch (IOException e) {
            Log.e(e.getMessage());
            return null;
        }
    }

}
