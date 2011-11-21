package org.musicbrainz.mobile.task;

import java.io.IOException;

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.base.DataQueryActivity;

public class LookupReleaseTask extends MusicBrainzTask {
    
    private Credentials creds;
    private Release release;
    private UserData userData;
    
    public LookupReleaseTask(DataQueryActivity activity) {
        super(activity);
        if (activity.isUserLoggedIn()) {
            creds = activity.getCredentials();
        }
    }
    
    @Override
    protected Void run(String... mbid) throws Exception {
        if (creds == null) {
            getRelease(mbid[0]);
        } else {
            getReleaseWithUserData(mbid[0]);
        }
        return null;
    }
    
    private void getRelease(String mbid) throws IOException {
        WebClient client = new WebClient(userAgent);
        release = client.lookupRelease(mbid);
    }
    
    private void getReleaseWithUserData(String mbid) throws IOException {
        WebClient client = new WebClient(creds);
        release = client.lookupRelease(mbid);
        userData = client.getUserData(MBEntity.RELEASE_GROUP, release.getReleaseGroupMbid());
    }
    
    public Release getRelease() {
        return release;
    }
    
    public UserData getUserData() {
        return userData;
    }

}
