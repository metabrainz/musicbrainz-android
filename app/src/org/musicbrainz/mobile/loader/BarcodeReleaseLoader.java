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

package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.loader.result.AsyncEntityResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.content.Context;

public class BarcodeReleaseLoader extends PersistingAsyncTaskLoader<AsyncEntityResult<Release>> {
    
    private Credentials creds;
    private String userAgent;
    private String barcode;

    public BarcodeReleaseLoader(Context context, String userAgent, String barcode) {
        super(context);
        this.userAgent = userAgent;
        this.barcode = barcode;
    }
    
    public BarcodeReleaseLoader(Context context, Credentials creds, String barcode) {
        super(context);
        this.creds = creds;
        this.barcode = barcode;
    }

    @Override
    public AsyncEntityResult<Release> loadInBackground() {
        try {
            return getAvailableData();
        } catch (Exception e) {
            return new AsyncEntityResult<Release>(LoaderStatus.EXCEPTION, e);
        }
    }
    
    private AsyncEntityResult<Release> getAvailableData() throws IOException {
        if (creds == null) {
            return getRelease();
        } else {
            return getReleaseWithUserData();
        }
    }

    private AsyncEntityResult<Release> getRelease() throws IOException {
        WebClient client = new WebClient(userAgent);
        Release release = client.lookupReleaseFromBarcode(barcode);
        data = new AsyncEntityResult<Release>(LoaderStatus.SUCCESS, release);
        return data;
    }
    
    private AsyncEntityResult<Release> getReleaseWithUserData() throws IOException {
        WebClient client = new WebClient(creds);
        Release release = client.lookupReleaseFromBarcode(barcode);
        UserData userData = client.lookupUserData(MBEntity.RELEASE_GROUP, release.getReleaseGroupMbid());
        data = new AsyncEntityResult<Release>(LoaderStatus.SUCCESS, release, userData);
        return data;
    }

}
