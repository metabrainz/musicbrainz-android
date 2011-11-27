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

import java.io.IOException;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.base.DataQueryActivity;

public class LookupArtistTask extends MusicBrainzTask {

    private Credentials creds;
    private Artist artist;
    private UserData userData;

    public LookupArtistTask(DataQueryActivity activity) {
        super(activity);
        if (activity.isUserLoggedIn()) {
            creds = activity.getCredentials();
        }
    }

    @Override
    protected Void run(String... mbid) throws Exception {
        if (creds == null) {
            getArtist(mbid[0]);
        } else {
            getArtistWithUserData(mbid[0]);
        }
        return null;
    }

    private void getArtist(String mbid) throws IOException {
        WebClient client = new WebClient(userAgent);
        artist = client.lookupArtist(mbid);
    }
    
    private void getArtistWithUserData(String mbid) throws IOException {
        WebClient client = new WebClient(creds);
        artist = client.lookupArtist(mbid);
        userData = client.getUserData(MBEntity.ARTIST, mbid);
    }
    
    public Artist getArtist() {
        return artist;
    }
    
    public UserData getUserData() {
        return userData;
    }
    
}
