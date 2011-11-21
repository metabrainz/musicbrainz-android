package org.musicbrainz.mobile.task;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.base.DataQueryActivity;

public class LookupRGStubsTask extends MusicBrainzTask {

    private LinkedList<ReleaseStub> stubs;
    
    public LookupRGStubsTask(DataQueryActivity activity) {
        super(activity);
    }

    @Override
    protected Void run(String... releaseGroupMbid) throws Exception {
        WebClient client = new WebClient(userAgent);
        stubs = client.browseReleases(releaseGroupMbid[0]);
        return null;
    }
    
    public LinkedList<ReleaseStub> getStubs() {
        return stubs;
    }

}
