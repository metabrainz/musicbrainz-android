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
import java.util.LinkedList;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.MusicBrainzApplication;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.content.Context;

public class CollectionsLoader extends PersistingAsyncTaskLoader<AsyncResult<LinkedList<EditorCollectionStub>>> {

    private MusicBrainzApplication app;

    public CollectionsLoader(Context appContext) {
        super(appContext);
        app = (MusicBrainzApplication) appContext;
    }

    @Override
    public AsyncResult<LinkedList<EditorCollectionStub>> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(app.getUserAgent());
            data = new AsyncResult<LinkedList<EditorCollectionStub>>(LoaderStatus.SUCCESS, client.lookupUserCollections());
            return data;
        } catch (IOException e) {
            return new AsyncResult<LinkedList<EditorCollectionStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

}
