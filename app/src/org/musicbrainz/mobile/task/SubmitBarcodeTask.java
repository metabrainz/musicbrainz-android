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
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.BarcodeSearchActivity;

import android.widget.Toast;

public class SubmitBarcodeTask extends IgnitedAsyncTask<BarcodeSearchActivity, String, Void, Void> {

    private Credentials creds;
    private String mbid;
    
    public SubmitBarcodeTask(BarcodeSearchActivity activity, String mbid) {
        creds = activity.getCredentials();
        this.mbid = mbid;
    }
    
    @Override
    protected void onStart(BarcodeSearchActivity activity) {
        activity.onStartSubmission();
    }
    
    @Override
    protected Void run(String... barcode) throws Exception {
        WebClient client = new WebClient(creds);
        client.submitBarcode(mbid, barcode[0]);
        return null;
    }
    
    @Override
    protected void onSuccess(BarcodeSearchActivity activity, Void v) {
        if (activity != null) {
            Toast.makeText(activity, R.string.toast_barcode, Toast.LENGTH_SHORT).show();
            activity.onSubmissionDone();
        }
    }
    
    @Override
    protected void onError(BarcodeSearchActivity activity, Exception e) {
        if (activity != null) {
            Toast.makeText(activity, R.string.toast_barcode_fail, Toast.LENGTH_LONG).show();
            activity.onSubmissionDone();
        }
    }
}

