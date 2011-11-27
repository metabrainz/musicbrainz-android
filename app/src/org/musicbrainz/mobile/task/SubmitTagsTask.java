/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.task;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.android.api.webservice.WebServiceUtils;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.base.TagRateActivity;

import android.widget.Toast;

public class SubmitTagsTask extends IgnitedAsyncTask<TagRateActivity, String, Void, Void> {
    
    private Credentials creds;
    private MBEntity type;
    private String mbid;
    private LinkedList<Tag> updatedTags;
    
    public SubmitTagsTask(TagRateActivity activity, MBEntity type, String mbid) {
        super(activity);
        creds = activity.getCredentials();
        this.type = type;
        this.mbid = mbid;
    }
    
    @Override
    protected void onStart(TagRateActivity activity) {
        activity.onStartTagging();
    }
    
    @Override
    protected Void run(String... tags) throws Exception {
        WebClient client = new WebClient(creds);
        LinkedList<String> saneTags = WebServiceUtils.sanitiseCommaSeparatedTags(tags[0]);
        client.submitTags(type, mbid, saneTags);
        updatedTags = client.lookupTags(type, mbid);
        return null;
    }
    
    @Override
    protected void onSuccess(TagRateActivity activity, Void v) {
        if (activity != null) {
            Toast.makeText(activity, R.string.toast_tag, Toast.LENGTH_SHORT).show();
            activity.onDoneTagging();
        }
    }

    @Override
    protected void onError(TagRateActivity activity, Exception e) {
        if (activity != null) {
            Toast.makeText(activity, R.string.toast_tag_fail, Toast.LENGTH_LONG).show();
            activity.onDoneTagging();
        }
    }
    
    public LinkedList<Tag> getUpdatedTags() {
        return updatedTags;
    }

}
