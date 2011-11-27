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

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.SearchActivity;

public class SearchArtistsTask extends MusicBrainzTask {

    private LinkedList<ArtistStub> results;

    public SearchArtistsTask(SearchActivity activity) {
        super(activity);
    }

    @Override
    protected Void run(String... term) throws Exception {
        WebClient client = new WebClient(userAgent);
        results = client.searchArtists(term[0]);
        return null;
    }
    
    public LinkedList<ArtistStub> getArtistResults() {
        return results;
    }
    
}
