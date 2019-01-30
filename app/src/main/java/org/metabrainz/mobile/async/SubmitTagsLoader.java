package org.metabrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.api.data.Tag;
import org.metabrainz.mobile.api.util.WebServiceUtils;
import org.metabrainz.mobile.api.webservice.Entity;
import org.metabrainz.mobile.App;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

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
