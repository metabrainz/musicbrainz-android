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

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.WebClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SearchReleaseLoader extends AsyncTaskLoader<AsyncResult<LinkedList<ReleaseStub>>> {

    private String userAgent;
    private String term;

    AsyncResult<LinkedList<ReleaseStub>> data;

    public SearchReleaseLoader(Context context, String userAgent, String term) {
        super(context);
        this.userAgent = userAgent;
        this.term = term;
    }

    @Override
    public AsyncResult<LinkedList<ReleaseStub>> loadInBackground() {
        try {
            WebClient client = new WebClient(userAgent);
            data = new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.SUCCESS, client.searchRelease(term));
            return data;
        } catch (IOException e) {
            return new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

    @Override
    public void deliverResult(AsyncResult<LinkedList<ReleaseStub>> data) {
        if (isReset()) {
            return;
        }
        this.data = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }
        if (takeContentChanged() || data == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        data = null;
    }

}
