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

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.ReleaseGroupStub;

public class SearchResults {
    
    private final SearchType type;
    private final LinkedList<ArtistStub> artistResults;
    private final LinkedList<ReleaseGroupStub> releaseGroupResults;
    
    @SuppressWarnings("unchecked")
    public SearchResults(SearchType type, LinkedList<?> results) {
        if (type == SearchType.ARTIST) {
            artistResults = (LinkedList<ArtistStub>) results;
            releaseGroupResults = null;
        } else {
            artistResults = null;
            releaseGroupResults = (LinkedList<ReleaseGroupStub>) results;
        }
        this.type = type;
    }
    
    public SearchResults(LinkedList<ArtistStub> artistResults, LinkedList<ReleaseGroupStub> releaseGroupResults) {
        this.artistResults = artistResults;
        this.releaseGroupResults = releaseGroupResults;
        this.type = SearchType.ALL;
    }

    public SearchType getType() {
        return type;
    }

    public LinkedList<ArtistStub> getArtistResults() {
        return artistResults;
    }

    public LinkedList<ReleaseGroupStub> getReleaseGroupResults() {
        return releaseGroupResults;
    }

    public enum SearchType {
        ARTIST, RELEASE_GROUP, ALL;
    }
}
