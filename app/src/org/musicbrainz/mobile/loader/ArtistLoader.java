package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncEntityResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

public class ArtistLoader extends PersistingAsyncTaskLoader<AsyncEntityResult<Artist>> {

    private String mbid;

    public ArtistLoader(String mbid) {
        this.mbid = mbid;
    }

    @Override
    public AsyncEntityResult<Artist> loadInBackground() {
        try {
            return getAvailableData();
        } catch (Exception e) {
            return new AsyncEntityResult<Artist>(LoaderStatus.EXCEPTION, e);
        }
    }

    private AsyncEntityResult<Artist> getAvailableData() throws IOException {
        if (App.isUserLoggedIn()) {
            return getArtistWithUserData();
        } else {
            return getArtist();
        }
    }

    private AsyncEntityResult<Artist> getArtist() throws IOException {
        MusicBrainz client = new MusicBrainzWebClient(App.getUserAgent());
        data = new AsyncEntityResult<Artist>(LoaderStatus.SUCCESS, client.lookupArtist(mbid));
        return data;
    }

    private AsyncEntityResult<Artist> getArtistWithUserData() throws IOException {
        MusicBrainz client = new MusicBrainzWebClient(App.getCredentials());
        Artist artist = client.lookupArtist(mbid);
        UserData userData = client.lookupUserData(Entity.ARTIST, mbid);
        data = new AsyncEntityResult<Artist>(LoaderStatus.SUCCESS, artist, userData);
        return data;
    }

}
