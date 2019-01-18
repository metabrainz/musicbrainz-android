package org.metabrainz.android.async;

import java.io.IOException;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.api.data.Artist;
import org.metabrainz.android.api.data.UserData;
import org.metabrainz.android.api.webservice.Entity;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncEntityResult;
import org.metabrainz.android.async.result.LoaderStatus;

public class ArtistLoader extends PersistingAsyncTaskLoader<AsyncEntityResult<Artist>> {

    private MusicBrainz client;
    private String mbid;

    public ArtistLoader(String mbid) {
        client = App.getWebClient();
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
        data = new AsyncEntityResult<Artist>(LoaderStatus.SUCCESS, client.lookupArtist(mbid));
        return data;
    }

    private AsyncEntityResult<Artist> getArtistWithUserData() throws IOException {
        Artist artist = client.lookupArtist(mbid);
        UserData userData = client.lookupUserData(Entity.ARTIST, mbid);
        data = new AsyncEntityResult<Artist>(LoaderStatus.SUCCESS, artist, userData);
        return data;
    }

}
