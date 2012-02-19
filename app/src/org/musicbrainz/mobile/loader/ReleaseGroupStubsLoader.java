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
import java.util.LinkedList;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.MusicBrainzApplication;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ReleaseGroupStubsLoader extends AsyncTaskLoader<AsyncResult<LinkedList<ReleaseStub>>> {

    private MusicBrainzApplication app;
    private String mbid;

    public ReleaseGroupStubsLoader(Context appContext, String mbid) {
        super(appContext);
        app = (MusicBrainzApplication) appContext;
        this.mbid = mbid;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<LinkedList<ReleaseStub>> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(app.getUserAgent());
            return new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.SUCCESS, client.browseReleases(mbid));
        } catch (IOException e) {
            return new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

}
