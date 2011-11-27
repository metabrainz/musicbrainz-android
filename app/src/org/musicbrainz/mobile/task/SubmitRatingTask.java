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

import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.base.TagRateActivity;

import android.widget.Toast;

public class SubmitRatingTask extends IgnitedAsyncTask<TagRateActivity, Integer, Void, Void> {

    private Credentials creds;
    private MBEntity type;
    private String mbid;
    private float updatedRating;
    
    public SubmitRatingTask(TagRateActivity activity, MBEntity type, String mbid) {
        super(activity);
        creds = activity.getCredentials();
        this.type = type;
        this.mbid = mbid;
    }
    
    @Override
    protected void onStart(TagRateActivity activity) {
        activity.onStartRating();
    }
    
    @Override
    protected Void run(Integer... rating) throws Exception {
        WebClient client = new WebClient(creds);
        client.submitRating(type, mbid, rating[0]);
        updatedRating = client.lookupRating(type, mbid);
        return null;
    }
    
    @Override
    protected void onSuccess(TagRateActivity activity, Void v) {
        if (activity != null) {
            Toast.makeText(activity, R.string.toast_rate, Toast.LENGTH_SHORT).show();
            activity.onDoneRating();
        }
    }

    @Override
    protected void onError(TagRateActivity activity, Exception e) {
        if (activity != null) {
            Toast.makeText(activity, R.string.toast_rate_fail, Toast.LENGTH_LONG).show();
            activity.onDoneRating();
        }
    }
    
    public float getUpdatedRating() {
        return updatedRating;
    }
    
}
