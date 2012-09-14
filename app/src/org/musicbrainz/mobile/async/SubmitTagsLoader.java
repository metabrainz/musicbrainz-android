package org.musicbrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.util.WebServiceUtils;
import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class SubmitTagsLoader extends AsyncTaskLoader<AsyncResult<List<Tag>>> {

    private MusicBrainz client;
    private Entity type;
    private String mbid;
    private String tags;

    public SubmitTagsLoader(Entity type, String mbid, String tags) {
        super(App.getContext());
        client = App.getWebClient();
        this.type = type;
        this.mbid = mbid;
        this.tags = tags;
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<List<Tag>> loadInBackground() {
        List<String> sanitisedTags = WebServiceUtils.sanitiseCommaSeparatedTags(tags);
        try {
            client.submitTags(type, mbid, sanitisedTags);
            List<Tag> updatedTags = client.lookupTags(type, mbid);
            return new AsyncResult<List<Tag>>(LoaderStatus.SUCCESS, updatedTags);
        } catch (IOException e) {
            return new AsyncResult<List<Tag>>(LoaderStatus.EXCEPTION, e);
        }
    }
}
