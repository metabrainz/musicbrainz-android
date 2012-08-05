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

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.loader.result.AsyncEntityResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

public class ArtistLoader extends PersistingAsyncTaskLoader<AsyncEntityResult<Artist>> {

    private String mbid;

    public ArtistLoader(String mbid) {
        this.mbid = mbid;
    }

    @Override
    public AsyncEntityResult<Artist> loadInBackground() {
        try {
            return getAvailableData();
        } catch (Exception e) {
            return new AsyncEntityResult<Artist>(LoaderStatus.EXCEPTION, e);
        }
    }

    private AsyncEntityResult<Artist> getAvailableData() throws IOException {
        if (MusicBrainzApp.isUserLoggedIn()) {
            return getArtistWithUserData();
        } else {
            return getArtist();
        }
    }

    private AsyncEntityResult<Artist> getArtist() throws IOException {
        MusicBrainz client = new MusicBrainzWebClient(MusicBrainzApp.getUserAgent());
        data = new AsyncEntityResult<Artist>(LoaderStatus.SUCCESS, client.lookupArtist(mbid));
        return data;
    }

    private AsyncEntityResult<Artist> getArtistWithUserData() throws IOException {
        MusicBrainz client = new MusicBrainzWebClient(MusicBrainzApp.getCredentials());
        Artist artist = client.lookupArtist(mbid);
        UserData userData = client.lookupUserData(Entity.ARTIST, mbid);
        data = new AsyncEntityResult<Artist>(LoaderStatus.SUCCESS, artist, userData);
        return data;
    }

}
