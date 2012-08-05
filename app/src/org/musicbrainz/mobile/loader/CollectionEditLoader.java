/*
 * Copyright (C) 2012 Jamie McDonald
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

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class CollectionEditLoader extends AsyncTaskLoader<AsyncResult<Void>> {
    
    private String collectionMbid;
    private String releaseMbid;
    private boolean isAdd;
    
    public CollectionEditLoader(String collectionMbid, String releaseMbid, boolean isAdd) {
        super(MusicBrainzApp.getContext());
        this.collectionMbid = collectionMbid;
        this.releaseMbid = releaseMbid;
        this.isAdd = isAdd;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<Void> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(MusicBrainzApp.getCredentials());
            if (isAdd) {
                client.addReleaseToCollection(collectionMbid, releaseMbid);
            } else {
                client.deleteReleaseFromCollection(collectionMbid, releaseMbid);
            }
            return new AsyncResult<Void>(LoaderStatus.SUCCESS);
        } catch (IOException e) {
            return new AsyncResult<Void>(LoaderStatus.EXCEPTION, e);
        }
    }

}
