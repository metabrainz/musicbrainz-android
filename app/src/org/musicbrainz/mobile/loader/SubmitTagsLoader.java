package org.musicbrainz.mobile.loader;

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.android.api.webservice.WebServiceUtils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SubmitTagsLoader extends AsyncTaskLoader<AsyncResult<LinkedList<Tag>>> {

    private Credentials creds;
    private MBEntity type;
    private String mbid;
    private String tags;

    public SubmitTagsLoader(Context context, Credentials creds, MBEntity type, String mbid, String tags) {
        super(context);
        this.creds = creds;
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
    public AsyncResult<LinkedList<Tag>> loadInBackground() {
        WebClient client = new WebClient(creds);
        LinkedList<String> saneTags = WebServiceUtils.sanitiseCommaSeparatedTags(tags);
        try {
            client.submitTags(type, mbid, saneTags);
            LinkedList<Tag> updatedTags = client.lookupTags(type, mbid);
            return new AsyncResult<LinkedList<Tag>>(LoaderStatus.SUCCESS, updatedTags);
        } catch (IOException e) {
            return new AsyncResult<LinkedList<Tag>>(LoaderStatus.EXCEPTION, e);
        }
    }

}
