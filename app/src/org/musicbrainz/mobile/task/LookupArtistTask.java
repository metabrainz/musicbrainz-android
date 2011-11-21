package org.musicbrainz.mobile.task;

import java.io.IOException;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.base.DataQueryActivity;

public class LookupArtistTask extends MusicBrainzTask {

    private Credentials creds;
    private Artist artist;
    private UserData userData;

    public LookupArtistTask(DataQueryActivity activity) {
        super(activity);
        if (activity.isUserLoggedIn()) {
            creds = activity.getCredentials();
        }
    }

    @Override
    protected Void run(String... mbid) throws Exception {
        if (creds == null) {
            getArtist(mbid[0]);
        } else {
            getArtistWithUserData(mbid[0]);
        }
        return null;
    }

    private void getArtist(String mbid) throws IOException {
        WebClient client = new WebClient(userAgent);
        artist = client.lookupArtist(mbid);
    }
    
    private void getArtistWithUserData(String mbid) throws IOException {
        WebClient client = new WebClient(creds);
        artist = client.lookupArtist(mbid);
        userData = client.getUserData(MBEntity.ARTIST, mbid);
    }
    
    public Artist getArtist() {
        return artist;
    }
    
    public UserData getUserData() {
        return userData;
    }
    
}
